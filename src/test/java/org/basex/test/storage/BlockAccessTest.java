package org.basex.test.storage;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.basex.build.DiskBuilder;
import org.basex.build.xml.XMLParser;
import org.basex.core.Prop;
import org.basex.core.proc.DropDB;
import org.basex.data.Data;
import org.basex.io.IO;
import org.basex.io.TableDiskAccess;
import static org.junit.Assert.*;
import static org.basex.data.DataText.*;

/**
 * This class tests the update functionality of the BlockStorage.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-10, ISC License
 * @author Tim Petrowsky
 */
public final class BlockAccessTest {
  /** Test file we do updates with. */
  private static final String TESTFILE = "src/main/resources/etc/xml/JUnit.xml";
  /** Test database name. */
  private static final String DBNAME =
    BlockAccessTest.class.getSimpleName();
  /** Test file we do updates with. */
  private static final Prop PROP = new Prop(true);
  /** BlockStorage. */
  private TableDiskAccess tba;
  /** Data reference. */
  private Data data;
  /** Test file size. */
  private int size;
  /** Starting storage. */
  private byte[] storage;
  /** Max entries per block. */
  private int entries;
  /** Expected blocks in file. */
  private int blocks;
  /** Nodes per block. */
  private int nodes;

  /**
   * Initializes the test class.
   */
  @BeforeClass
  public static void setUpBeforeClass() {
    PROP.set(Prop.TEXTINDEX, false);
    PROP.set(Prop.ATTRINDEX, false);
  }

  /**
   * Loads the JUnitTest database.
   */
  @Before
  public void setUp() {
    try {
      final XMLParser parser = new XMLParser(IO.get(TESTFILE), PROP);
      data = new DiskBuilder(parser).build(DBNAME);
      size = data.meta.size;
      data.close();
      tba = new TableDiskAccess(data.meta, DATATBL);
    } catch(final Exception ex) {
      ex.printStackTrace();
    }

    final int bytecount = size * (1 << IO.NODEPOWER);
    storage = new byte[bytecount];
    for(int i = 0; i < bytecount; i++) {
      storage[i] = (byte) tba.read1(i >> IO.NODEPOWER, i % (1 << IO.NODEPOWER));
    }
    entries = IO.BLOCKSIZE >>> IO.NODEPOWER;
    blocks = (int) Math.ceil(size / Math.floor(entries * IO.BLOCKFILL));
    nodes = (int) (entries * IO.BLOCKFILL);
  }

  /**
   * Drops the JUnitTest database.
   */
  @After
  public void tearDown() {
    try {
      tba.close();
      DropDB.drop(DBNAME, PROP);
    } catch(final Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Closes and reloads storage.
   */
  private void closeAndReload() {
    try {
      tba.close();
      tba = new TableDiskAccess(data.meta, DATATBL);
    } catch(final IOException ex) {
      fail();
    }
  }

  /**
   * Compares old with new entries.
   * @param startNodeNumber first old entry to compare
   * @param currentNodeNumber first new entry to compare
   * @param count number of entries to compare
   */
  private void assertEntrysEqual(final int startNodeNumber,
      final int currentNodeNumber, final int count) {
    final int startOffset = startNodeNumber << IO.NODEPOWER;
    final int currentOffset = currentNodeNumber << IO.NODEPOWER;
    for(int i = 0; i < count << IO.NODEPOWER; i++) {
      final int startByteNum = startOffset + i;
      final int currentByteNum = currentOffset + i;
      final byte startByte = storage[startByteNum];
      final byte currentByte = (byte) tba.read1(currentByteNum >> IO.NODEPOWER,
        currentByteNum % (1 << IO.NODEPOWER));
      assertEquals("Old entry " + (startByteNum >> IO.NODEPOWER)
          + " (byte " + startByteNum % (1 << IO.NODEPOWER)
          + ") and new entry " + (currentByteNum >> IO.NODEPOWER)
          + " (byte " + currentByteNum % (1 << IO.NODEPOWER) + ")",
          startByte, currentByte);
    }
  }

  /**
   * Tests size of file.
   */
  @Test
  public void testSize() {
    assertEquals("Testfile size changed!", size, tba.size());
    assertTrue("Need at least 3 blocks for testing!", blocks > 2);
    assertEquals("Unexpected number of blocks!", blocks, tba.blocks());
    closeAndReload();
    assertEquals("Testfile size changed!", size, tba.size());
    assertTrue("Need at least 3 blocks for testing!", blocks > 2);
    assertEquals("Unexpected number of blocks!", blocks, tba.blocks());
  }

  /**
   * Tests delete.
   */
  @Test
  public void testDeleteOneNode() {
    tba.delete(3, 1);
    assertEquals("One node deleted => size-1", size - 1, tba.size());
    assertEntrysEqual(0, 0, 3);
    assertEntrysEqual(4, 3, size - 4);
    closeAndReload();
    assertEquals("One node deleted => size-1", size - 1, tba.size());
    assertEntrysEqual(0, 0, 3);
    assertEntrysEqual(4, 3, size - 4);
  }

  /**
   * Tests delete at beginning.
   */
  @Test
  public void testDeleteAtBeginning() {
    tba.delete(0, 3);
    assertEquals("Three nodes deleted => size-3", size - 3, tba.size());
    assertEntrysEqual(3, 0, size - 3);
    closeAndReload();
    assertEquals("Three nodes deleted => size-3", size - 3, tba.size());
    assertEntrysEqual(3, 0, size - 3);
  }

  /**
   * Tests delete at end.
   */
  @Test
  public void testDeleteAtEnd() {
    tba.delete(size - 3, 3);
    assertEquals("Three nodes deleted => size-3", size - 3, tba.size());
    assertEntrysEqual(0, 0, size - 3);
    closeAndReload();
    assertEquals("Three nodes deleted => size-3", size - 3, tba.size());
    assertEntrysEqual(0, 0, size - 3);
  }

  /**
   * Deletes first block.
   */
  @Test
  public void testDeleteFirstBlock() {
    tba.delete(0, nodes);
    assertEquals(blocks - 1, tba.blocks());
    assertEntrysEqual(nodes, 0, size - nodes);
    closeAndReload();
    assertEquals(blocks - 1, tba.blocks());
    assertEntrysEqual(nodes, 0, size - nodes);
  }

  /**
   * Deletes the second block.
   */
  @Test
  public void testDeleteSecondBlock() {
    tba.delete(nodes, nodes);
    assertEquals(blocks - 1, tba.blocks());
    assertEntrysEqual(0, 0, nodes);
    assertEntrysEqual(2 * nodes, nodes, size - 2
        * nodes);
    closeAndReload();
    assertEquals(blocks - 1, tba.blocks());
    assertEntrysEqual(0, 0, nodes);
    assertEntrysEqual(2 * nodes, nodes, size - 2
        * nodes);
  }

  /**
   * Deletes the last block.
   */
  @Test
  public void testDeleteLastBlock() {
    tba.delete(size / nodes * nodes, size % nodes);
    assertEquals(blocks - 1, tba.blocks());
    assertEntrysEqual(0, 0, nodes - size % nodes);
    closeAndReload();
    assertEquals(blocks - 1, tba.blocks());
    assertEntrysEqual(0, 0, nodes - size % nodes);
  }

  /**
   * Deletes the second block with some surrounding nodes.
   */
  @Test
  public void testDeleteSecondBlockAndSurroundingNodes() {
    tba.delete(nodes - 1, nodes + 2);
    assertEquals(size - 2 - nodes, tba.size());
    assertEquals(blocks - 1, tba.blocks());
    assertEntrysEqual(0, 0, nodes - 1);
    assertEntrysEqual(2 * nodes + 1, nodes - 1, size - 2
        * nodes - 1);
    closeAndReload();
    assertEquals(size - 2 - nodes, tba.size());
    assertEquals(blocks - 1, tba.blocks());
    assertEntrysEqual(0, 0, nodes - 1);
    assertEntrysEqual(2 * nodes + 1, nodes - 1, size - 2
        * nodes - 1);
  }

  /**
   * Tests basic insertion.
   */
  @Test
  public void testSimpleInsert() {
    tba.insert(4, getTestEntries(1));
    assertEquals(size + 1, tba.size());
    assertEntrysEqual(0, 0, 4);
    assertAreInserted(4, 1);
    assertEntrysEqual(4, 5, size - 4);
    closeAndReload();
    assertEquals(size + 1, tba.size());
    assertEntrysEqual(0, 0, 4);
    assertAreInserted(4, 1);
    assertEntrysEqual(4, 5, size - 4);
  }

  /**
   * Tests inserting multiple entries.
   */
  @Test
  public void testInsertMultiple() {
    tba.insert(4, getTestEntries(3));
    assertEquals(size + 3, tba.size());
    assertEntrysEqual(0, 0, 4);
    assertAreInserted(4, 3);
    assertEntrysEqual(4, 7, size - 4);
    closeAndReload();
    assertEquals(size + 3, tba.size());
    assertEntrysEqual(0, 0, 4);
    assertAreInserted(4, 3);
    assertEntrysEqual(4, 7, size - 4);
  }

  /**
   * Tests inserting multiple entries.
   */
  @Test
  public void testInsertMany() {
    tba.insert(4, getTestEntries(entries - 1));
    assertEquals(size + entries - 1, tba.size());
    assertEntrysEqual(0, 0, 4);
    assertAreInserted(4, entries - 1);
    assertEntrysEqual(4, 4 + entries - 1, size - 4);
    closeAndReload();
    assertEquals(size + entries - 1, tba.size());
    assertEntrysEqual(0, 0, 4);
    assertAreInserted(4, entries - 1);
    assertEntrysEqual(4, 4 + entries - 1, size - 4);
  }

  /**
   * Tests inserting multiple entries.
   */
  @Test
  public void testInsertAtBlockBoundary() {
    tba.insert(nodes, getTestEntries(nodes));
    assertEquals(size + nodes, tba.size());
    assertEquals(blocks + 1, tba.blocks());
    assertEntrysEqual(0, 0, nodes);
    assertAreInserted(nodes, nodes);
    assertEntrysEqual(nodes, 2 * nodes, size - nodes);
    closeAndReload();
    assertEquals(size + nodes, tba.size());
    assertEquals(blocks + 1, tba.blocks());
    assertEntrysEqual(0, 0, nodes);
    assertAreInserted(nodes, nodes);
    assertEntrysEqual(nodes, 2 * nodes, size - nodes);
  }

  /**
   * Asserts that the chosen entries are inserted by a test case.
   * @param startNum first entry
   * @param count number of entries
   */
  private void assertAreInserted(final int startNum, final int count) {
    for(int i = 0; i < count; i++)
      for(int j = 0; j < 1 << IO.NODEPOWER; j++)
        assertEquals(5, tba.read1(startNum + i, j));
  }

  /**
   * Creates a test-byte array containing the specified number of entries.
   * All bytes are set to (byte) 5.
   * @param e number of entries to create
   * @return byte array containing the number of entries (all bytes 5)
   */
  private byte[] getTestEntries(final int e) {
    final byte[] result = new byte[e << IO.NODEPOWER];
    for(int i = 0; i < result.length; i++) result[i] = 5;
    return result;
  }
}
