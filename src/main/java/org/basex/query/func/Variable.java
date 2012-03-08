package org.basex.query.func;

import static org.basex.query.QueryText.*;

import java.io.File;

import org.basex.query.*;
import org.basex.query.item.QNm;
import org.basex.query.item.Str;
import org.basex.query.item.Value;
import org.basex.query.util.*;
import org.basex.query.util.Var.VarKind;
import org.basex.util.TokenBuilder;

/**
 * Statically available XQuery variables.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public enum Variable {

  /** File variable. */
  FILEDIRSEP(FILEURI, "directory-separator", Str.get(File.separator)),
  /** File variable. */
  FILEPATHSEP(FILEURI, "path-separator", Str.get(File.pathSeparator)),

  /** XSLT variable. */
  XSLTPROC(XSLTURI, "processor", Str.get(FNXslt.get(true))),
  /** XSLT variable. */
  XSLTVERSION(XSLTURI, "version", Str.get(FNXslt.get(false)));

  /** Variable name. */
  private final QNm qname;
  /** Variable value. */
  private final Value value;

  /**
   * Constructor.
   * @param uri uri
   * @param name name
   * @param val item value
   */
  Variable(final byte[] uri, final String name, final Value val) {
    qname = new QNm(name, uri);
    value = val;
  }

  @Override
  public final String toString() {
    final byte[] pref = NSGlobal.prefix(qname.uri());
    return new TokenBuilder("$").add(pref).add(':').add(
        qname.local()).toString();
  }

  /**
   * Tries to find the variable with the given name.
   * @param name name of the variable
   * @param ctx query context
   * @return variable if found, {@code null} otherwise
   * @throws QueryException exception
   */
  public static Var get(final QNm name, final QueryContext ctx) throws QueryException {
    for(final Variable v : values())
      if(v.qname.eq(name)) {
        final Var var = new Var(ctx, v.qname, v.value.type(), VarKind.GLOBAL);
        ctx.globals.set(ctx, null, var, null, v.value, true);
        return var;
      }
    return null;
  }
}
