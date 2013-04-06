  


<!DOCTYPE html>
<html>
  <head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# githubog: http://ogp.me/ns/fb/githubog#">
    <meta charset='utf-8'>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>basex/src/main/java/org/basex/gui/layout/TableLayout.java at master · BaseXdb/basex</title>
    <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="GitHub" />
    <link rel="fluid-icon" href="https://github.com/fluidicon.png" title="GitHub" />
    <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-114.png" />
    <link rel="apple-touch-icon" sizes="114x114" href="/apple-touch-icon-114.png" />
    <link rel="apple-touch-icon" sizes="72x72" href="/apple-touch-icon-144.png" />
    <link rel="apple-touch-icon" sizes="144x144" href="/apple-touch-icon-144.png" />
    <link rel="logo" type="image/svg" href="http://github-media-downloads.s3.amazonaws.com/github-logo.svg" />
    <link rel="xhr-socket" href="/_sockets">
    <meta name="msapplication-TileImage" content="/windows-tile.png">
    <meta name="msapplication-TileColor" content="#ffffff">

    
    
    <link rel="icon" type="image/x-icon" href="/favicon.ico" />

    <meta content="authenticity_token" name="csrf-param" />
<meta content="/z7/OUpUUctUviRTIyPk2zHgdL4/fbq5qS6MEX4vdiQ=" name="csrf-token" />

    <link href="https://a248.e.akamai.net/assets.github.com/assets/github-8424f74a551e0aa008a2555ebdb672c36b471576.css" media="all" rel="stylesheet" type="text/css" />
    <link href="https://a248.e.akamai.net/assets.github.com/assets/github2-4ee688f9a05c8f1a8143eb8b0de6aaf41e591dce.css" media="all" rel="stylesheet" type="text/css" />
    


      <script src="https://a248.e.akamai.net/assets.github.com/assets/frameworks-bafee0a321be765ed924edd4c746d8af68510845.js" type="text/javascript"></script>
      <script src="https://a248.e.akamai.net/assets.github.com/assets/github-d171b4626ad1a1a31b20a934792ff29a6a8fbb01.js" type="text/javascript"></script>
      
      <meta http-equiv="x-pjax-version" content="8f696c960cc0114719739c65386e4c33">

        <link data-pjax-transient rel='permalink' href='/BaseXdb/basex/blob/967e2f434dd69499429ba365803998d3144546a7/src/main/java/org/basex/gui/layout/TableLayout.java'>
    <meta property="og:title" content="basex"/>
    <meta property="og:type" content="githubog:gitrepository"/>
    <meta property="og:url" content="https://github.com/BaseXdb/basex"/>
    <meta property="og:image" content="https://secure.gravatar.com/avatar/624672c2cdbb441e2a91250910feb772?s=420&amp;d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png"/>
    <meta property="og:site_name" content="GitHub"/>
    <meta property="og:description" content="basex - BaseX Main Repository."/>
    <meta property="twitter:card" content="summary"/>
    <meta property="twitter:site" content="@GitHub">
    <meta property="twitter:title" content="BaseXdb/basex"/>

    <meta name="description" content="basex - BaseX Main Repository." />

  <link href="https://github.com/BaseXdb/basex/commits/master.atom" rel="alternate" title="Recent Commits to basex:master" type="application/atom+xml" />

  </head>


  <body class="logged_in page-blob linux vis-public env-production  ">
    <div id="wrapper">

      

      
      
      

      <div class="header header-logged-in true">
  <div class="container clearfix">

    <a class="header-logo-blacktocat" href="https://github.com/">
  <span class="mega-icon mega-icon-blacktocat"></span>
</a>

    <div class="divider-vertical"></div>

    
  <a href="/notifications" class="notification-indicator tooltipped downwards" title="You have unread notifications">
    <span class="mail-status unread"></span>
  </a>
  <div class="divider-vertical"></div>


      <div class="command-bar js-command-bar  ">
            <form accept-charset="UTF-8" action="/search" class="command-bar-form" id="top_search_form" method="get">
  <a href="/search/advanced" class="advanced-search-icon tooltipped downwards command-bar-search" id="advanced_search" title="Advanced search"><span class="mini-icon mini-icon-advanced-search "></span></a>

  <input type="text" data-hotkey="/ s" name="q" id="js-command-bar-field" placeholder="Search or type a command" tabindex="1" data-username="Masoumeh" autocapitalize="off">

  <span class="mini-icon help tooltipped downwards" title="Show command bar help">
    <span class="mini-icon mini-icon-help"></span>
  </span>

  <input type="hidden" name="ref" value="cmdform">

    <input type="hidden" class="js-repository-name-with-owner" value="BaseXdb/basex"/>
    <input type="hidden" class="js-repository-branch" value="master"/>
    <input type="hidden" class="js-repository-tree-sha" value="91ea262f6b8253185a179ad45401e3f61eeff948"/>

  <div class="divider-vertical"></div>
</form>
        <ul class="top-nav">
            <li class="explore"><a href="https://github.com/explore">Explore</a></li>
            <li><a href="https://gist.github.com">Gist</a></li>
            <li><a href="/blog">Blog</a></li>
          <li><a href="http://help.github.com">Help</a></li>
        </ul>
      </div>

    

  
    <ul id="user-links">
      <li>
        <a href="https://github.com/Masoumeh" class="name">
          <img height="20" src="https://secure.gravatar.com/avatar/75c20941909c1cd824a9544532c21e9a?s=140&amp;d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png" width="20" /> Masoumeh
        </a>
      </li>
      <li>
        <a href="/new" id="new_repo" class="tooltipped downwards" title="Create a new repo">
          <span class="mini-icon mini-icon-create"></span>
        </a>
      </li>
      <li>
        <a href="/settings/profile" id="account_settings"
          class="tooltipped downwards"
          title="Account settings ">
          <span class="mini-icon mini-icon-account-settings"></span>
        </a>
      </li>
      <li>
        <a href="/logout" data-method="post" id="logout" class="tooltipped downwards" title="Sign out">
          <span class="mini-icon mini-icon-logout"></span>
        </a>
      </li>
    </ul>



    
  </div>
</div>

      

      

      


            <div class="site hfeed" itemscope itemtype="http://schema.org/WebPage">
      <div class="hentry">
        
        <div class="pagehead repohead instapaper_ignore readability-menu ">
          <div class="container">
            <div class="title-actions-bar">
              


<ul class="pagehead-actions">

    <li class="nspr">
      <a href="/BaseXdb/basex/pull/new/master" class="button minibutton btn-pull-request" icon_class="mini-icon-pull-request"><span class="mini-icon mini-icon-pull-request"></span>Pull Request</a>
    </li>

    <li class="subscription">
      <form accept-charset="UTF-8" action="/notifications/subscribe" data-autosubmit="true" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="authenticity_token" type="hidden" value="/z7/OUpUUctUviRTIyPk2zHgdL4/fbq5qS6MEX4vdiQ=" /></div>  <input id="repository_id" name="repository_id" type="hidden" value="1374129" />

    <div class="select-menu js-menu-container js-select-menu">
      <span class="minibutton select-menu-button js-menu-target">
        <span class="js-select-button">
          <span class="mini-icon mini-icon-watching"></span>
          Watch
        </span>
      </span>

      <div class="select-menu-modal-holder js-menu-content">
        <div class="select-menu-modal">
          <div class="select-menu-header">
            <span class="select-menu-title">Notification status</span>
            <span class="mini-icon mini-icon-remove-close js-menu-close"></span>
          </div> <!-- /.select-menu-header -->

          <div class="select-menu-list js-navigation-container">

            <div class="select-menu-item js-navigation-item js-navigation-target selected">
              <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
              <div class="select-menu-item-text">
                <input checked="checked" id="do_included" name="do" type="radio" value="included" />
                <h4>Not watching</h4>
                <span class="description">You only receive notifications for discussions in which you participate or are @mentioned.</span>
                <span class="js-select-button-text hidden-select-button-text">
                  <span class="mini-icon mini-icon-watching"></span>
                  Watch
                </span>
              </div>
            </div> <!-- /.select-menu-item -->

            <div class="select-menu-item js-navigation-item js-navigation-target ">
              <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
              <div class="select-menu-item-text">
                <input id="do_subscribed" name="do" type="radio" value="subscribed" />
                <h4>Watching</h4>
                <span class="description">You receive notifications for all discussions in this repository.</span>
                <span class="js-select-button-text hidden-select-button-text">
                  <span class="mini-icon mini-icon-unwatch"></span>
                  Unwatch
                </span>
              </div>
            </div> <!-- /.select-menu-item -->

            <div class="select-menu-item js-navigation-item js-navigation-target ">
              <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
              <div class="select-menu-item-text">
                <input id="do_ignore" name="do" type="radio" value="ignore" />
                <h4>Ignoring</h4>
                <span class="description">You do not receive any notifications for discussions in this repository.</span>
                <span class="js-select-button-text hidden-select-button-text">
                  <span class="mini-icon mini-icon-mute"></span>
                  Stop ignoring
                </span>
              </div>
            </div> <!-- /.select-menu-item -->

          </div> <!-- /.select-menu-list -->

        </div> <!-- /.select-menu-modal -->
      </div> <!-- /.select-menu-modal-holder -->
    </div> <!-- /.select-menu -->

</form>
    </li>

    <li class="js-toggler-container js-social-container starring-container ">
      <a href="/BaseXdb/basex/unstar" class="minibutton js-toggler-target star-button starred upwards" title="Unstar this repo" data-remote="true" data-method="post" rel="nofollow">
        <span class="mini-icon mini-icon-remove-star"></span>
        <span class="text">Unstar</span>
      </a>
      <a href="/BaseXdb/basex/star" class="minibutton js-toggler-target star-button unstarred upwards" title="Star this repo" data-remote="true" data-method="post" rel="nofollow">
        <span class="mini-icon mini-icon-star"></span>
        <span class="text">Star</span>
      </a>
      <a class="social-count js-social-count" href="/BaseXdb/basex/stargazers">104</a>
    </li>

        <li>
          <a href="/BaseXdb/basex/fork_select" class="minibutton js-toggler-target fork-button lighter upwards" title="Fork this repo" rel="facebox nofollow">
            <span class="mini-icon mini-icon-branch-create"></span>
            <span class="text">Fork</span>
          </a>
          <a href="/BaseXdb/basex/network" class="social-count">50</a>
        </li>


</ul>

              <h1 itemscope itemtype="http://data-vocabulary.org/Breadcrumb" class="entry-title public">
                <span class="repo-label"><span>public</span></span>
                <span class="mega-icon mega-icon-public-repo"></span>
                <span class="author vcard">
                  <a href="/BaseXdb" class="url fn" itemprop="url" rel="author">
                  <span itemprop="title">BaseXdb</span>
                  </a></span> /
                <strong><a href="/BaseXdb/basex" class="js-current-repository">basex</a></strong>
              </h1>
            </div>

            
  <ul class="tabs">
    <li><a href="/BaseXdb/basex" class="selected" highlight="repo_source repo_downloads repo_commits repo_tags repo_branches">Code</a></li>
    <li><a href="/BaseXdb/basex/network" highlight="repo_network">Network</a></li>
    <li><a href="/BaseXdb/basex/pulls" highlight="repo_pulls">Pull Requests <span class='counter'>1</span></a></li>

      <li><a href="/BaseXdb/basex/issues" highlight="repo_issues">Issues <span class='counter'>26</span></a></li>

      <li><a href="/BaseXdb/basex/wiki" highlight="repo_wiki">Wiki</a></li>


    <li><a href="/BaseXdb/basex/graphs" highlight="repo_graphs repo_contributors">Graphs</a></li>


  </ul>
  
<div class="tabnav">

  <span class="tabnav-right">
    <ul class="tabnav-tabs">
          <li><a href="/BaseXdb/basex/tags" class="tabnav-tab" highlight="repo_tags">Tags <span class="counter ">22</span></a></li>
    </ul>
    
  </span>

  <div class="tabnav-widget scope">


    <div class="select-menu js-menu-container js-select-menu js-branch-menu">
      <a class="minibutton select-menu-button js-menu-target" data-hotkey="w" data-ref="master">
        <span class="mini-icon mini-icon-branch"></span>
        <i>branch:</i>
        <span class="js-select-button">master</span>
      </a>

      <div class="select-menu-modal-holder js-menu-content js-navigation-container">

        <div class="select-menu-modal">
          <div class="select-menu-header">
            <span class="select-menu-title">Switch branches/tags</span>
            <span class="mini-icon mini-icon-remove-close js-menu-close"></span>
          </div> <!-- /.select-menu-header -->

          <div class="select-menu-filters">
            <div class="select-menu-text-filter">
              <input type="text" id="commitish-filter-field" class="js-filterable-field js-navigation-enable" placeholder="Find or create a branch…">
            </div>
            <div class="select-menu-tabs">
              <ul>
                <li class="select-menu-tab">
                  <a href="#" data-tab-filter="branches" class="js-select-menu-tab">Branches</a>
                </li>
                <li class="select-menu-tab">
                  <a href="#" data-tab-filter="tags" class="js-select-menu-tab">Tags</a>
                </li>
              </ul>
            </div><!-- /.select-menu-tabs -->
          </div><!-- /.select-menu-filters -->

          <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket css-truncate" data-tab-filter="branches">

            <div data-filterable-for="commitish-filter-field" data-filterable-type="substring">

                <div class="select-menu-item js-navigation-item js-navigation-target selected">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/master/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="master" rel="nofollow" title="master">master</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/stack-frames/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="stack-frames" rel="nofollow" title="stack-frames">stack-frames</a>
                </div> <!-- /.select-menu-item -->
            </div>

              <form accept-charset="UTF-8" action="/BaseXdb/basex/branches" class="js-create-branch select-menu-item select-menu-new-item-form js-navigation-item js-navigation-target js-new-item-form" method="post"><div style="margin:0;padding:0;display:inline"><input name="authenticity_token" type="hidden" value="/z7/OUpUUctUviRTIyPk2zHgdL4/fbq5qS6MEX4vdiQ=" /></div>

                <span class="mini-icon mini-icon-branch-create select-menu-item-icon"></span>
                <div class="select-menu-item-text">
                  <h4>Create branch: <span class="js-new-item-name"></span></h4>
                  <span class="description">from ‘master’</span>
                </div>
                <input type="hidden" name="name" id="name" class="js-new-item-value">
                <input type="hidden" name="branch" id="branch" value="master" />
                <input type="hidden" name="path" id="branch" value="src/main/java/org/basex/gui/layout/TableLayout.java" />
              </form> <!-- /.select-menu-item -->

          </div> <!-- /.select-menu-list -->


          <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket css-truncate" data-tab-filter="tags">
            <div data-filterable-for="commitish-filter-field" data-filterable-type="substring">

                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/7.6/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="7.6" rel="nofollow" title="7.6">7.6</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/7.5/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="7.5" rel="nofollow" title="7.5">7.5</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/7.3/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="7.3" rel="nofollow" title="7.3">7.3</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/7.2.1/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="7.2.1" rel="nofollow" title="7.2.1">7.2.1</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/7.2/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="7.2" rel="nofollow" title="7.2">7.2</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/7.1.1/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="7.1.1" rel="nofollow" title="7.1.1">7.1.1</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/7.1/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="7.1" rel="nofollow" title="7.1">7.1</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/7.0.2/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="7.0.2" rel="nofollow" title="7.0.2">7.0.2</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/7.0.1/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="7.0.1" rel="nofollow" title="7.0.1">7.0.1</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/7.0/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="7.0" rel="nofollow" title="7.0">7.0</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/6.7.1/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="6.7.1" rel="nofollow" title="6.7.1">6.7.1</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/6.7/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="6.7" rel="nofollow" title="6.7">6.7</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/6.6.2/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="6.6.2" rel="nofollow" title="6.6.2">6.6.2</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/6.6.1/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="6.6.1" rel="nofollow" title="6.6.1">6.6.1</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/6.6/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="6.6" rel="nofollow" title="6.6">6.6</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/6.5.1/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="6.5.1" rel="nofollow" title="6.5.1">6.5.1</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/6.5/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="6.5" rel="nofollow" title="6.5">6.5</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/6.3/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="6.3" rel="nofollow" title="6.3">6.3</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/6.2/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="6.2" rel="nofollow" title="6.2">6.2</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/6.1/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="6.1" rel="nofollow" title="6.1">6.1</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/6.0/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="6.0" rel="nofollow" title="6.0">6.0</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item js-navigation-target ">
                  <span class="select-menu-item-icon mini-icon mini-icon-confirm"></span>
                  <a href="/BaseXdb/basex/blob/5.0/src/main/java/org/basex/gui/layout/TableLayout.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="5.0" rel="nofollow" title="5.0">5.0</a>
                </div> <!-- /.select-menu-item -->
            </div>

            <div class="select-menu-no-results">Nothing to show</div>

          </div> <!-- /.select-menu-list -->

        </div> <!-- /.select-menu-modal -->
      </div> <!-- /.select-menu-modal-holder -->
    </div> <!-- /.select-menu -->

  </div> <!-- /.scope -->

  <ul class="tabnav-tabs">
    <li><a href="/BaseXdb/basex" class="selected tabnav-tab" highlight="repo_source">Files</a></li>
    <li><a href="/BaseXdb/basex/commits/master" class="tabnav-tab" highlight="repo_commits">Commits</a></li>
    <li><a href="/BaseXdb/basex/branches" class="tabnav-tab" highlight="repo_branches" rel="nofollow">Branches <span class="counter ">2</span></a></li>
  </ul>

</div>

  
  
  


            
          </div>
        </div><!-- /.repohead -->

        <div id="js-repo-pjax-container" class="container context-loader-container" data-pjax-container>
          


<!-- blob contrib key: blob_contributors:v21:a7bcc62a7bd37f5784d6cd125d716cb1 -->
<!-- blob contrib frag key: views10/v8/blob_contributors:v21:a7bcc62a7bd37f5784d6cd125d716cb1 -->


<div id="slider">
    <div class="frame-meta">

      <p title="This is a placeholder element" class="js-history-link-replace hidden"></p>

        <div class="breadcrumb">
          <span class='bold'><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/BaseXdb/basex" class="js-slide-to" data-branch="master" data-direction="back" itemscope="url"><span itemprop="title">basex</span></a></span></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/BaseXdb/basex/tree/master/src" class="js-slide-to" data-branch="master" data-direction="back" itemscope="url"><span itemprop="title">src</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/BaseXdb/basex/tree/master/src/main" class="js-slide-to" data-branch="master" data-direction="back" itemscope="url"><span itemprop="title">main</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/BaseXdb/basex/tree/master/src/main/java" class="js-slide-to" data-branch="master" data-direction="back" itemscope="url"><span itemprop="title">java</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/BaseXdb/basex/tree/master/src/main/java/org" class="js-slide-to" data-branch="master" data-direction="back" itemscope="url"><span itemprop="title">org</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/BaseXdb/basex/tree/master/src/main/java/org/basex" class="js-slide-to" data-branch="master" data-direction="back" itemscope="url"><span itemprop="title">basex</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/BaseXdb/basex/tree/master/src/main/java/org/basex/gui" class="js-slide-to" data-branch="master" data-direction="back" itemscope="url"><span itemprop="title">gui</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/BaseXdb/basex/tree/master/src/main/java/org/basex/gui/layout" class="js-slide-to" data-branch="master" data-direction="back" itemscope="url"><span itemprop="title">layout</span></a></span><span class="separator"> / </span><strong class="final-path">TableLayout.java</strong> <span class="js-zeroclipboard zeroclipboard-button" data-clipboard-text="src/main/java/org/basex/gui/layout/TableLayout.java" data-copied-hint="copied!" title="copy to clipboard"><span class="mini-icon mini-icon-clipboard"></span></span>
        </div>

      <a href="/BaseXdb/basex/find/master" class="js-slide-to" data-hotkey="t" style="display:none">Show File Finder</a>


        <div class="commit commit-loader file-history-tease js-deferred-content" data-url="/BaseXdb/basex/contributors/master/src/main/java/org/basex/gui/layout/TableLayout.java">
          Fetching contributors…

          <div class="participation">
            <p class="loader-loading"><img alt="Octocat-spinner-32-eaf2f5" height="16" src="https://a248.e.akamai.net/assets.github.com/images/spinners/octocat-spinner-32-EAF2F5.gif?1360648847" width="16" /></p>
            <p class="loader-error">Cannot retrieve contributors at this time</p>
          </div>
        </div>

    </div><!-- ./.frame-meta -->

    <div class="frames">
      <div class="frame" data-permalink-url="/BaseXdb/basex/blob/967e2f434dd69499429ba365803998d3144546a7/src/main/java/org/basex/gui/layout/TableLayout.java" data-title="basex/src/main/java/org/basex/gui/layout/TableLayout.java at master · BaseXdb/basex · GitHub" data-type="blob">

        <div id="files" class="bubble">
          <div class="file">
            <div class="meta">
              <div class="info">
                <span class="icon"><b class="mini-icon mini-icon-text-file"></b></span>
                <span class="mode" title="File Mode">file</span>
                  <span>122 lines (108 sloc)</span>
                <span>3.606 kb</span>
              </div>
              <div class="actions">
                <div class="button-group">
                        <a class="minibutton"
                           href="/BaseXdb/basex/edit/master/src/main/java/org/basex/gui/layout/TableLayout.java"
                           data-method="post" rel="nofollow" data-hotkey="e">Edit</a>
                  <a href="/BaseXdb/basex/raw/master/src/main/java/org/basex/gui/layout/TableLayout.java" class="button minibutton " id="raw-url">Raw</a>
                    <a href="/BaseXdb/basex/blame/master/src/main/java/org/basex/gui/layout/TableLayout.java" class="button minibutton ">Blame</a>
                  <a href="/BaseXdb/basex/commits/master/src/main/java/org/basex/gui/layout/TableLayout.java" class="button minibutton " rel="nofollow">History</a>
                </div><!-- /.button-group -->
              </div><!-- /.actions -->

            </div>
                <div class="data type-java js-blob-data">
      <table cellpadding="0" cellspacing="0" class="lines">
        <tr>
          <td>
            <pre class="line_numbers"><span id="L1" rel="#L1">1</span>
<span id="L2" rel="#L2">2</span>
<span id="L3" rel="#L3">3</span>
<span id="L4" rel="#L4">4</span>
<span id="L5" rel="#L5">5</span>
<span id="L6" rel="#L6">6</span>
<span id="L7" rel="#L7">7</span>
<span id="L8" rel="#L8">8</span>
<span id="L9" rel="#L9">9</span>
<span id="L10" rel="#L10">10</span>
<span id="L11" rel="#L11">11</span>
<span id="L12" rel="#L12">12</span>
<span id="L13" rel="#L13">13</span>
<span id="L14" rel="#L14">14</span>
<span id="L15" rel="#L15">15</span>
<span id="L16" rel="#L16">16</span>
<span id="L17" rel="#L17">17</span>
<span id="L18" rel="#L18">18</span>
<span id="L19" rel="#L19">19</span>
<span id="L20" rel="#L20">20</span>
<span id="L21" rel="#L21">21</span>
<span id="L22" rel="#L22">22</span>
<span id="L23" rel="#L23">23</span>
<span id="L24" rel="#L24">24</span>
<span id="L25" rel="#L25">25</span>
<span id="L26" rel="#L26">26</span>
<span id="L27" rel="#L27">27</span>
<span id="L28" rel="#L28">28</span>
<span id="L29" rel="#L29">29</span>
<span id="L30" rel="#L30">30</span>
<span id="L31" rel="#L31">31</span>
<span id="L32" rel="#L32">32</span>
<span id="L33" rel="#L33">33</span>
<span id="L34" rel="#L34">34</span>
<span id="L35" rel="#L35">35</span>
<span id="L36" rel="#L36">36</span>
<span id="L37" rel="#L37">37</span>
<span id="L38" rel="#L38">38</span>
<span id="L39" rel="#L39">39</span>
<span id="L40" rel="#L40">40</span>
<span id="L41" rel="#L41">41</span>
<span id="L42" rel="#L42">42</span>
<span id="L43" rel="#L43">43</span>
<span id="L44" rel="#L44">44</span>
<span id="L45" rel="#L45">45</span>
<span id="L46" rel="#L46">46</span>
<span id="L47" rel="#L47">47</span>
<span id="L48" rel="#L48">48</span>
<span id="L49" rel="#L49">49</span>
<span id="L50" rel="#L50">50</span>
<span id="L51" rel="#L51">51</span>
<span id="L52" rel="#L52">52</span>
<span id="L53" rel="#L53">53</span>
<span id="L54" rel="#L54">54</span>
<span id="L55" rel="#L55">55</span>
<span id="L56" rel="#L56">56</span>
<span id="L57" rel="#L57">57</span>
<span id="L58" rel="#L58">58</span>
<span id="L59" rel="#L59">59</span>
<span id="L60" rel="#L60">60</span>
<span id="L61" rel="#L61">61</span>
<span id="L62" rel="#L62">62</span>
<span id="L63" rel="#L63">63</span>
<span id="L64" rel="#L64">64</span>
<span id="L65" rel="#L65">65</span>
<span id="L66" rel="#L66">66</span>
<span id="L67" rel="#L67">67</span>
<span id="L68" rel="#L68">68</span>
<span id="L69" rel="#L69">69</span>
<span id="L70" rel="#L70">70</span>
<span id="L71" rel="#L71">71</span>
<span id="L72" rel="#L72">72</span>
<span id="L73" rel="#L73">73</span>
<span id="L74" rel="#L74">74</span>
<span id="L75" rel="#L75">75</span>
<span id="L76" rel="#L76">76</span>
<span id="L77" rel="#L77">77</span>
<span id="L78" rel="#L78">78</span>
<span id="L79" rel="#L79">79</span>
<span id="L80" rel="#L80">80</span>
<span id="L81" rel="#L81">81</span>
<span id="L82" rel="#L82">82</span>
<span id="L83" rel="#L83">83</span>
<span id="L84" rel="#L84">84</span>
<span id="L85" rel="#L85">85</span>
<span id="L86" rel="#L86">86</span>
<span id="L87" rel="#L87">87</span>
<span id="L88" rel="#L88">88</span>
<span id="L89" rel="#L89">89</span>
<span id="L90" rel="#L90">90</span>
<span id="L91" rel="#L91">91</span>
<span id="L92" rel="#L92">92</span>
<span id="L93" rel="#L93">93</span>
<span id="L94" rel="#L94">94</span>
<span id="L95" rel="#L95">95</span>
<span id="L96" rel="#L96">96</span>
<span id="L97" rel="#L97">97</span>
<span id="L98" rel="#L98">98</span>
<span id="L99" rel="#L99">99</span>
<span id="L100" rel="#L100">100</span>
<span id="L101" rel="#L101">101</span>
<span id="L102" rel="#L102">102</span>
<span id="L103" rel="#L103">103</span>
<span id="L104" rel="#L104">104</span>
<span id="L105" rel="#L105">105</span>
<span id="L106" rel="#L106">106</span>
<span id="L107" rel="#L107">107</span>
<span id="L108" rel="#L108">108</span>
<span id="L109" rel="#L109">109</span>
<span id="L110" rel="#L110">110</span>
<span id="L111" rel="#L111">111</span>
<span id="L112" rel="#L112">112</span>
<span id="L113" rel="#L113">113</span>
<span id="L114" rel="#L114">114</span>
<span id="L115" rel="#L115">115</span>
<span id="L116" rel="#L116">116</span>
<span id="L117" rel="#L117">117</span>
<span id="L118" rel="#L118">118</span>
<span id="L119" rel="#L119">119</span>
<span id="L120" rel="#L120">120</span>
<span id="L121" rel="#L121">121</span>
</pre>
          </td>
          <td width="100%">
                  <div class="highlight"><pre><div class='line' id='LC1'><span class="kn">package</span> <span class="n">org</span><span class="o">.</span><span class="na">basex</span><span class="o">.</span><span class="na">gui</span><span class="o">.</span><span class="na">layout</span><span class="o">;</span></div><div class='line' id='LC2'><br/></div><div class='line' id='LC3'><span class="kn">import</span> <span class="nn">java.awt.*</span><span class="o">;</span></div><div class='line' id='LC4'><br/></div><div class='line' id='LC5'><span class="cm">/**</span></div><div class='line' id='LC6'><span class="cm"> * This LayoutManager is similar to the GridLayout. The added components</span></div><div class='line' id='LC7'><span class="cm"> * keep their minimum size even when the parent container is resized.</span></div><div class='line' id='LC8'><span class="cm"> *</span></div><div class='line' id='LC9'><span class="cm"> * @author BaseX Team 2005-12, BSD License</span></div><div class='line' id='LC10'><span class="cm"> * @author Christian Gruen</span></div><div class='line' id='LC11'><span class="cm"> */</span></div><div class='line' id='LC12'><span class="kd">public</span> <span class="kd">final</span> <span class="kd">class</span> <span class="nc">TableLayout</span> <span class="kd">implements</span> <span class="n">LayoutManager</span> <span class="o">{</span></div><div class='line' id='LC13'>&nbsp;&nbsp;<span class="cm">/** Number of columns. */</span></div><div class='line' id='LC14'>&nbsp;&nbsp;<span class="kd">private</span> <span class="kd">final</span> <span class="kt">int</span> <span class="n">cols</span><span class="o">;</span></div><div class='line' id='LC15'>&nbsp;&nbsp;<span class="cm">/** Number of rows. */</span></div><div class='line' id='LC16'>&nbsp;&nbsp;<span class="kd">private</span> <span class="kd">final</span> <span class="kt">int</span> <span class="n">rows</span><span class="o">;</span></div><div class='line' id='LC17'>&nbsp;&nbsp;<span class="cm">/** Horizontal inset. */</span></div><div class='line' id='LC18'>&nbsp;&nbsp;<span class="kd">private</span> <span class="kd">final</span> <span class="kt">int</span> <span class="n">insetX</span><span class="o">;</span></div><div class='line' id='LC19'>&nbsp;&nbsp;<span class="cm">/** Vertical inset. */</span></div><div class='line' id='LC20'>&nbsp;&nbsp;<span class="kd">private</span> <span class="kd">final</span> <span class="kt">int</span> <span class="n">insetY</span><span class="o">;</span></div><div class='line' id='LC21'>&nbsp;&nbsp;<span class="cm">/** Panel width. */</span></div><div class='line' id='LC22'>&nbsp;&nbsp;<span class="kd">private</span> <span class="kt">int</span> <span class="n">width</span><span class="o">;</span></div><div class='line' id='LC23'>&nbsp;&nbsp;<span class="cm">/** Panel height. */</span></div><div class='line' id='LC24'>&nbsp;&nbsp;<span class="kd">private</span> <span class="kt">int</span> <span class="n">height</span><span class="o">;</span></div><div class='line' id='LC25'>&nbsp;&nbsp;<span class="cm">/** Horizontal position. */</span></div><div class='line' id='LC26'>&nbsp;&nbsp;<span class="kd">private</span> <span class="kd">final</span> <span class="kt">int</span><span class="o">[]</span> <span class="n">posX</span><span class="o">;</span></div><div class='line' id='LC27'>&nbsp;&nbsp;<span class="cm">/** Vertical position. */</span></div><div class='line' id='LC28'>&nbsp;&nbsp;<span class="kd">private</span> <span class="kd">final</span> <span class="kt">int</span><span class="o">[]</span> <span class="n">posY</span><span class="o">;</span></div><div class='line' id='LC29'><br/></div><div class='line' id='LC30'>&nbsp;&nbsp;<span class="cm">/**</span></div><div class='line' id='LC31'><span class="cm">   * Creates a grid layout with the specified number of rows and columns.</span></div><div class='line' id='LC32'><span class="cm">   * When displayed, the grid has the minimum size.</span></div><div class='line' id='LC33'><span class="cm">   * @param r number of rows</span></div><div class='line' id='LC34'><span class="cm">   * @param c number of columns</span></div><div class='line' id='LC35'><span class="cm">   */</span></div><div class='line' id='LC36'>&nbsp;&nbsp;<span class="kd">public</span> <span class="nf">TableLayout</span><span class="o">(</span><span class="kd">final</span> <span class="kt">int</span> <span class="n">r</span><span class="o">,</span> <span class="kd">final</span> <span class="kt">int</span> <span class="n">c</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC37'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">this</span><span class="o">(</span><span class="n">r</span><span class="o">,</span> <span class="n">c</span><span class="o">,</span> <span class="mi">0</span><span class="o">,</span> <span class="mi">0</span><span class="o">);</span></div><div class='line' id='LC38'>&nbsp;&nbsp;<span class="o">}</span></div><div class='line' id='LC39'><br/></div><div class='line' id='LC40'>&nbsp;&nbsp;<span class="cm">/**</span></div><div class='line' id='LC41'><span class="cm">   * Creates a grid layout with the specified number of rows and columns.</span></div><div class='line' id='LC42'><span class="cm">   * When displayed, the grid has the minimum size.</span></div><div class='line' id='LC43'><span class="cm">   * @param r number of rows</span></div><div class='line' id='LC44'><span class="cm">   * @param c number of columns</span></div><div class='line' id='LC45'><span class="cm">   * @param ix horizontal gap</span></div><div class='line' id='LC46'><span class="cm">   * @param iy vertical gap</span></div><div class='line' id='LC47'><span class="cm">   */</span></div><div class='line' id='LC48'>&nbsp;&nbsp;<span class="kd">public</span> <span class="nf">TableLayout</span><span class="o">(</span><span class="kd">final</span> <span class="kt">int</span> <span class="n">r</span><span class="o">,</span> <span class="kd">final</span> <span class="kt">int</span> <span class="n">c</span><span class="o">,</span> <span class="kd">final</span> <span class="kt">int</span> <span class="n">ix</span><span class="o">,</span> <span class="kd">final</span> <span class="kt">int</span> <span class="n">iy</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC49'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">rows</span> <span class="o">=</span> <span class="n">r</span><span class="o">;</span></div><div class='line' id='LC50'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">cols</span> <span class="o">=</span> <span class="n">c</span><span class="o">;</span></div><div class='line' id='LC51'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">insetX</span> <span class="o">=</span> <span class="n">ix</span><span class="o">;</span></div><div class='line' id='LC52'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">insetY</span> <span class="o">=</span> <span class="n">iy</span><span class="o">;</span></div><div class='line' id='LC53'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">posX</span> <span class="o">=</span> <span class="k">new</span> <span class="kt">int</span><span class="o">[</span><span class="n">c</span><span class="o">];</span></div><div class='line' id='LC54'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">posY</span> <span class="o">=</span> <span class="k">new</span> <span class="kt">int</span><span class="o">[</span><span class="n">r</span><span class="o">];</span></div><div class='line' id='LC55'>&nbsp;&nbsp;<span class="o">}</span></div><div class='line' id='LC56'><br/></div><div class='line' id='LC57'>&nbsp;&nbsp;<span class="nd">@Override</span></div><div class='line' id='LC58'>&nbsp;&nbsp;<span class="kd">public</span> <span class="kt">void</span> <span class="nf">addLayoutComponent</span><span class="o">(</span><span class="kd">final</span> <span class="n">String</span> <span class="n">name</span><span class="o">,</span> <span class="kd">final</span> <span class="n">Component</span> <span class="n">comp</span><span class="o">)</span> <span class="o">{</span> <span class="o">}</span></div><div class='line' id='LC59'><br/></div><div class='line' id='LC60'>&nbsp;&nbsp;<span class="nd">@Override</span></div><div class='line' id='LC61'>&nbsp;&nbsp;<span class="kd">public</span> <span class="kt">void</span> <span class="nf">removeLayoutComponent</span><span class="o">(</span><span class="kd">final</span> <span class="n">Component</span> <span class="n">comp</span><span class="o">)</span> <span class="o">{</span> <span class="o">}</span></div><div class='line' id='LC62'><br/></div><div class='line' id='LC63'>&nbsp;&nbsp;<span class="nd">@Override</span></div><div class='line' id='LC64'>&nbsp;&nbsp;<span class="kd">public</span> <span class="n">Dimension</span> <span class="nf">preferredLayoutSize</span><span class="o">(</span><span class="kd">final</span> <span class="n">Container</span> <span class="n">parent</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC65'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">synchronized</span><span class="o">(</span><span class="n">parent</span><span class="o">.</span><span class="na">getTreeLock</span><span class="o">())</span> <span class="o">{</span></div><div class='line' id='LC66'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="n">Insets</span> <span class="n">in</span> <span class="o">=</span> <span class="n">parent</span><span class="o">.</span><span class="na">getInsets</span><span class="o">();</span></div><div class='line' id='LC67'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="kt">int</span> <span class="n">nr</span> <span class="o">=</span> <span class="n">parent</span><span class="o">.</span><span class="na">getComponentCount</span><span class="o">();</span></div><div class='line' id='LC68'><br/></div><div class='line' id='LC69'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kt">int</span> <span class="n">maxW</span> <span class="o">=</span> <span class="mi">0</span><span class="o">;</span></div><div class='line' id='LC70'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kt">int</span> <span class="n">maxH</span> <span class="o">=</span> <span class="mi">0</span><span class="o">;</span></div><div class='line' id='LC71'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">for</span><span class="o">(</span><span class="kt">int</span> <span class="n">i</span> <span class="o">=</span> <span class="mi">0</span><span class="o">;</span> <span class="n">i</span> <span class="o">&lt;</span> <span class="n">cols</span><span class="o">;</span> <span class="o">++</span><span class="n">i</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC72'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">posX</span><span class="o">[</span><span class="n">i</span><span class="o">]</span> <span class="o">=</span> <span class="n">maxW</span><span class="o">;</span></div><div class='line' id='LC73'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="kt">int</span> <span class="n">w</span> <span class="o">=</span> <span class="n">maxW</span><span class="o">;</span></div><div class='line' id='LC74'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kt">int</span> <span class="n">h</span> <span class="o">=</span> <span class="mi">0</span><span class="o">;</span></div><div class='line' id='LC75'><br/></div><div class='line' id='LC76'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">for</span><span class="o">(</span><span class="kt">int</span> <span class="n">j</span> <span class="o">=</span> <span class="mi">0</span><span class="o">;</span> <span class="n">j</span> <span class="o">&lt;</span> <span class="n">rows</span><span class="o">;</span> <span class="o">++</span><span class="n">j</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC77'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="kt">int</span> <span class="n">n</span> <span class="o">=</span> <span class="n">j</span> <span class="o">*</span> <span class="n">cols</span> <span class="o">+</span> <span class="n">i</span><span class="o">;</span></div><div class='line' id='LC78'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">if</span><span class="o">(</span><span class="n">n</span> <span class="o">&gt;=</span> <span class="n">nr</span><span class="o">)</span> <span class="k">break</span><span class="o">;</span></div><div class='line' id='LC79'><br/></div><div class='line' id='LC80'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="n">Component</span> <span class="n">c</span> <span class="o">=</span> <span class="n">parent</span><span class="o">.</span><span class="na">getComponent</span><span class="o">(</span><span class="n">n</span><span class="o">);</span></div><div class='line' id='LC81'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="n">Dimension</span> <span class="n">d</span> <span class="o">=</span> <span class="n">c</span><span class="o">.</span><span class="na">getPreferredSize</span><span class="o">();</span></div><div class='line' id='LC82'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">if</span><span class="o">(</span><span class="n">maxW</span> <span class="o">&lt;</span> <span class="n">w</span> <span class="o">+</span> <span class="n">d</span><span class="o">.</span><span class="na">width</span><span class="o">)</span> <span class="n">maxW</span> <span class="o">=</span> <span class="n">w</span> <span class="o">+</span> <span class="n">d</span><span class="o">.</span><span class="na">width</span><span class="o">;</span></div><div class='line' id='LC83'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">if</span><span class="o">(</span><span class="n">posY</span><span class="o">[</span><span class="n">j</span><span class="o">]</span> <span class="o">&lt;</span> <span class="n">h</span><span class="o">)</span> <span class="n">posY</span><span class="o">[</span><span class="n">j</span><span class="o">]</span> <span class="o">=</span> <span class="n">h</span><span class="o">;</span></div><div class='line' id='LC84'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">else</span> <span class="n">h</span> <span class="o">=</span> <span class="n">posY</span><span class="o">[</span><span class="n">j</span><span class="o">];</span></div><div class='line' id='LC85'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">h</span> <span class="o">+=</span> <span class="n">d</span><span class="o">.</span><span class="na">height</span><span class="o">;</span></div><div class='line' id='LC86'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="o">}</span></div><div class='line' id='LC87'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">if</span><span class="o">(</span><span class="n">maxH</span> <span class="o">&lt;</span> <span class="n">h</span><span class="o">)</span> <span class="n">maxH</span> <span class="o">=</span> <span class="n">h</span><span class="o">;</span></div><div class='line' id='LC88'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="o">}</span></div><div class='line' id='LC89'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">width</span> <span class="o">=</span> <span class="n">in</span><span class="o">.</span><span class="na">left</span> <span class="o">+</span> <span class="n">maxW</span> <span class="o">+</span> <span class="o">(</span><span class="n">cols</span> <span class="o">-</span> <span class="mi">1</span><span class="o">)</span> <span class="o">*</span> <span class="n">insetX</span> <span class="o">+</span> <span class="n">in</span><span class="o">.</span><span class="na">right</span><span class="o">;</span></div><div class='line' id='LC90'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">height</span> <span class="o">=</span> <span class="n">in</span><span class="o">.</span><span class="na">top</span> <span class="o">+</span> <span class="n">maxH</span> <span class="o">+</span> <span class="o">(</span><span class="n">rows</span> <span class="o">-</span> <span class="mi">1</span><span class="o">)</span> <span class="o">*</span> <span class="n">insetY</span> <span class="o">+</span> <span class="n">in</span><span class="o">.</span><span class="na">bottom</span><span class="o">;</span></div><div class='line' id='LC91'><br/></div><div class='line' id='LC92'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">return</span> <span class="k">new</span> <span class="nf">Dimension</span><span class="o">(</span><span class="n">width</span><span class="o">,</span> <span class="n">height</span><span class="o">);</span></div><div class='line' id='LC93'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="o">}</span></div><div class='line' id='LC94'>&nbsp;&nbsp;<span class="o">}</span></div><div class='line' id='LC95'><br/></div><div class='line' id='LC96'>&nbsp;&nbsp;<span class="nd">@Override</span></div><div class='line' id='LC97'>&nbsp;&nbsp;<span class="kd">public</span> <span class="n">Dimension</span> <span class="nf">minimumLayoutSize</span><span class="o">(</span><span class="kd">final</span> <span class="n">Container</span> <span class="n">parent</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC98'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">return</span> <span class="nf">preferredLayoutSize</span><span class="o">(</span><span class="n">parent</span><span class="o">);</span></div><div class='line' id='LC99'>&nbsp;&nbsp;<span class="o">}</span></div><div class='line' id='LC100'><br/></div><div class='line' id='LC101'>&nbsp;&nbsp;<span class="nd">@Override</span></div><div class='line' id='LC102'>&nbsp;&nbsp;<span class="kd">public</span> <span class="kt">void</span> <span class="nf">layoutContainer</span><span class="o">(</span><span class="kd">final</span> <span class="n">Container</span> <span class="n">p</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC103'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">preferredLayoutSize</span><span class="o">(</span><span class="n">p</span><span class="o">);</span></div><div class='line' id='LC104'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">synchronized</span><span class="o">(</span><span class="n">p</span><span class="o">.</span><span class="na">getTreeLock</span><span class="o">())</span> <span class="o">{</span></div><div class='line' id='LC105'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="n">Insets</span> <span class="n">in</span> <span class="o">=</span> <span class="n">p</span><span class="o">.</span><span class="na">getInsets</span><span class="o">();</span></div><div class='line' id='LC106'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="kt">int</span> <span class="n">nr</span> <span class="o">=</span> <span class="n">p</span><span class="o">.</span><span class="na">getComponentCount</span><span class="o">();</span></div><div class='line' id='LC107'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">for</span><span class="o">(</span><span class="kt">int</span> <span class="n">j</span> <span class="o">=</span> <span class="mi">0</span><span class="o">;</span> <span class="n">j</span> <span class="o">&lt;</span> <span class="n">rows</span><span class="o">;</span> <span class="o">++</span><span class="n">j</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC108'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">for</span><span class="o">(</span><span class="kt">int</span> <span class="n">i</span> <span class="o">=</span> <span class="mi">0</span><span class="o">;</span> <span class="n">i</span> <span class="o">&lt;</span> <span class="n">cols</span><span class="o">;</span> <span class="o">++</span><span class="n">i</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC109'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="kt">int</span> <span class="n">n</span> <span class="o">=</span> <span class="n">j</span> <span class="o">*</span> <span class="n">cols</span> <span class="o">+</span> <span class="n">i</span><span class="o">;</span></div><div class='line' id='LC110'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="k">if</span><span class="o">(</span><span class="n">n</span> <span class="o">&gt;=</span> <span class="n">nr</span><span class="o">)</span> <span class="k">return</span><span class="o">;</span></div><div class='line' id='LC111'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="n">Dimension</span> <span class="n">cs</span> <span class="o">=</span> <span class="n">p</span><span class="o">.</span><span class="na">getComponent</span><span class="o">(</span><span class="n">n</span><span class="o">).</span><span class="na">getPreferredSize</span><span class="o">();</span></div><div class='line' id='LC112'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="kt">int</span> <span class="n">x</span> <span class="o">=</span> <span class="n">in</span><span class="o">.</span><span class="na">left</span> <span class="o">+</span> <span class="n">posX</span><span class="o">[</span><span class="n">i</span><span class="o">]</span> <span class="o">+</span> <span class="n">i</span> <span class="o">*</span> <span class="n">insetX</span><span class="o">;</span></div><div class='line' id='LC113'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="kt">int</span> <span class="n">y</span> <span class="o">=</span> <span class="n">in</span><span class="o">.</span><span class="na">top</span> <span class="o">+</span> <span class="n">posY</span><span class="o">[</span><span class="n">j</span><span class="o">]</span> <span class="o">+</span> <span class="n">j</span> <span class="o">*</span> <span class="n">insetY</span><span class="o">;</span></div><div class='line' id='LC114'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="kt">int</span> <span class="n">w</span> <span class="o">=</span> <span class="n">cs</span><span class="o">.</span><span class="na">width</span> <span class="o">&gt;</span> <span class="mi">0</span> <span class="o">?</span> <span class="n">cs</span><span class="o">.</span><span class="na">width</span> <span class="o">:</span> <span class="n">width</span> <span class="o">-</span> <span class="n">in</span><span class="o">.</span><span class="na">left</span> <span class="o">-</span> <span class="n">in</span><span class="o">.</span><span class="na">right</span><span class="o">;</span></div><div class='line' id='LC115'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="kd">final</span> <span class="kt">int</span> <span class="n">h</span> <span class="o">=</span> <span class="n">cs</span><span class="o">.</span><span class="na">height</span> <span class="o">&gt;</span> <span class="mi">0</span> <span class="o">?</span> <span class="n">cs</span><span class="o">.</span><span class="na">height</span> <span class="o">:</span> <span class="n">height</span> <span class="o">-</span> <span class="n">in</span><span class="o">.</span><span class="na">top</span> <span class="o">-</span> <span class="n">in</span><span class="o">.</span><span class="na">bottom</span><span class="o">;</span></div><div class='line' id='LC116'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="n">p</span><span class="o">.</span><span class="na">getComponent</span><span class="o">(</span><span class="n">n</span><span class="o">).</span><span class="na">setBounds</span><span class="o">(</span><span class="n">x</span><span class="o">,</span> <span class="n">y</span><span class="o">,</span> <span class="n">w</span><span class="o">,</span> <span class="n">h</span><span class="o">);</span></div><div class='line' id='LC117'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="o">}</span></div><div class='line' id='LC118'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="o">}</span></div><div class='line' id='LC119'>&nbsp;&nbsp;&nbsp;&nbsp;<span class="o">}</span></div><div class='line' id='LC120'>&nbsp;&nbsp;<span class="o">}</span></div><div class='line' id='LC121'><span class="o">}</span></div></pre></div>
          </td>
        </tr>
      </table>
  </div>

          </div>
        </div>

        <a href="#jump-to-line" rel="facebox" data-hotkey="l" class="js-jump-to-line" style="display:none">Jump to Line</a>
        <div id="jump-to-line" style="display:none">
          <h2>Jump to Line</h2>
          <form accept-charset="UTF-8" class="js-jump-to-line-form">
            <input class="textfield js-jump-to-line-field" type="text">
            <div class="full-button">
              <button type="submit" class="button">Go</button>
            </div>
          </form>
        </div>

      </div>
    </div>
</div>

<div id="js-frame-loading-template" class="frame frame-loading large-loading-area" style="display:none;">
  <img class="js-frame-loading-spinner" src="https://a248.e.akamai.net/assets.github.com/images/spinners/octocat-spinner-128.gif?1360648847" height="64" width="64">
</div>


        </div>
      </div>
      <div class="context-overlay"></div>
    </div>

      <div id="footer-push"></div><!-- hack for sticky footer -->
    </div><!-- end of wrapper - hack for sticky footer -->

      <!-- footer -->
      <div id="footer">
  <div class="container clearfix">

      <dl class="footer_nav">
        <dt>GitHub</dt>
        <dd><a href="https://github.com/about">About us</a></dd>
        <dd><a href="https://github.com/blog">Blog</a></dd>
        <dd><a href="https://github.com/contact">Contact &amp; support</a></dd>
        <dd><a href="http://enterprise.github.com/">GitHub Enterprise</a></dd>
        <dd><a href="http://status.github.com/">Site status</a></dd>
      </dl>

      <dl class="footer_nav">
        <dt>Applications</dt>
        <dd><a href="http://mac.github.com/">GitHub for Mac</a></dd>
        <dd><a href="http://windows.github.com/">GitHub for Windows</a></dd>
        <dd><a href="http://eclipse.github.com/">GitHub for Eclipse</a></dd>
        <dd><a href="http://mobile.github.com/">GitHub mobile apps</a></dd>
      </dl>

      <dl class="footer_nav">
        <dt>Services</dt>
        <dd><a href="http://get.gaug.es/">Gauges: Web analytics</a></dd>
        <dd><a href="http://speakerdeck.com">Speaker Deck: Presentations</a></dd>
        <dd><a href="https://gist.github.com">Gist: Code snippets</a></dd>
        <dd><a href="http://jobs.github.com/">Job board</a></dd>
      </dl>

      <dl class="footer_nav">
        <dt>Documentation</dt>
        <dd><a href="http://help.github.com/">GitHub Help</a></dd>
        <dd><a href="http://developer.github.com/">Developer API</a></dd>
        <dd><a href="http://github.github.com/github-flavored-markdown/">GitHub Flavored Markdown</a></dd>
        <dd><a href="http://pages.github.com/">GitHub Pages</a></dd>
      </dl>

      <dl class="footer_nav">
        <dt>More</dt>
        <dd><a href="http://training.github.com/">Training</a></dd>
        <dd><a href="https://github.com/edu">Students &amp; teachers</a></dd>
        <dd><a href="http://shop.github.com">The Shop</a></dd>
        <dd><a href="/plans">Plans &amp; pricing</a></dd>
        <dd><a href="http://octodex.github.com/">The Octodex</a></dd>
      </dl>

      <hr class="footer-divider">


    <p class="right">&copy; 2013 <span title="0.12947s from fe4.rs.github.com">GitHub</span>, Inc. All rights reserved.</p>
    <a class="left" href="https://github.com/">
      <span class="mega-icon mega-icon-invertocat"></span>
    </a>
    <ul id="legal">
        <li><a href="https://github.com/site/terms">Terms of Service</a></li>
        <li><a href="https://github.com/site/privacy">Privacy</a></li>
        <li><a href="https://github.com/security">Security</a></li>
    </ul>

  </div><!-- /.container -->

</div><!-- /.#footer -->


    <div class="fullscreen-overlay js-fullscreen-overlay" id="fullscreen_overlay">
  <div class="fullscreen-container js-fullscreen-container">
    <div class="textarea-wrap">
      <textarea name="fullscreen-contents" id="fullscreen-contents" class="js-fullscreen-contents" placeholder="" data-suggester="fullscreen_suggester"></textarea>
          <div class="suggester-container">
              <div class="suggester fullscreen-suggester js-navigation-container" id="fullscreen_suggester"
                 data-url="/BaseXdb/basex/suggestions/commit">
              </div>
          </div>
    </div>
  </div>
  <div class="fullscreen-sidebar">
    <a href="#" class="exit-fullscreen js-exit-fullscreen tooltipped leftwards" title="Exit Zen Mode">
      <span class="mega-icon mega-icon-normalscreen"></span>
    </a>
    <a href="#" class="theme-switcher js-theme-switcher tooltipped leftwards"
      title="Switch themes">
      <span class="mini-icon mini-icon-brightness"></span>
    </a>
  </div>
</div>



    <div id="ajax-error-message" class="flash flash-error">
      <span class="mini-icon mini-icon-exclamation"></span>
      Something went wrong with that request. Please try again.
      <a href="#" class="mini-icon mini-icon-remove-close ajax-error-dismiss"></a>
    </div>

    
    
    <span id='server_response_time' data-time='0.13010' data-host='fe4'></span>
    
  </body>
</html>

