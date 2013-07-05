<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
                      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
  <title>Grails Console</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <con:resources/>

  <style>
    #header {
      background: #495666;
      padding: 14px;
      border-bottom: 8px solid #3D4754;
    }

    #header img {
      float: left;
      padding-bottom: 0px;
      padding-left: 12px;
      padding-right: 36px;
    }

    #header h1 {
      margin: 0px;
      padding: 0px;

      background: #495666;
      font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
      font-variant: normal;
      font-size: 1.3em;
      font-weight: normal;
      padding-top: 12px;
    }

    #header h2 {
      margin: 0px;
      padding: 0px;

      background: #495666;
      font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
      font-variant: normal;
      font-size: 0.9em;
      color: orange;
      font-weight: normal;
      padding-top: 3px;
    }

  </style>
</head>

<body>

<div id="header">
  <r:img dir='images' file='logo.jpg' plugin="aafApplicationBase" alt="${message(code:'branding.application.name')}" width="102" height="50" />
  <h1><g:message code="branding.application.name" encodeAs="HTML"/></h1>
  <h2>Administration Console</h2>

  <div class="buttons">
    <div class="buttonset">
      <button class="first selected horizontal button" title="Horizontal">
        <img src="${resource(dir: 'images', file: 'h.png', plugin: 'console')}" alt="Vertical"/>
      </button>
      <button class="last vertical button" title="Vertical">
        <img src="${resource(dir: 'images', file: 'v.png', plugin: 'console')}" alt="Horizontal"/>
      </button>
    </div>
  </div>
</div>

<div id="editor" style="display: none">
  <div class="buttons">
    <button class="submit button" title="(Ctrl + Enter)">Execute</button>

    <input type='text' name='filename' id='filename' />
    <button class='fromFile button'>Execute from file:</button>
  </div>

  <div id="code-wrapper">
    <g:textArea name="code" value="${code}" rows="25" cols="100"/>
  </div>

</div>

<div class="east results" style="display: none">
  <div class="buttons">
    <button class="clear button" title="(Esc)">Clear</button>
    <label class="wrap"><input type="checkbox" /> <span>Wrap text</span></label>
  </div>

  <div id="result"><div class="inner"></div></div>
</div>

<div class="south" style="display: none"></div>

<con:layoutResources/>
<script type="text/javascript" charset="utf-8">
window.gconsole = {
  pluginContext: "${resource(plugin: 'console')}",
  executeLink: "${createLink(action: 'execute')}"
}
</script>

</body>
</html>
