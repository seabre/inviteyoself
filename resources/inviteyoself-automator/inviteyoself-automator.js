var casper = require('casper').create({
  viewportSize: {width: 950, height: 950},
  pageSettings: {
    userAgent: 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36'
  }
});

var subdomain = casper.cli.get("subdomain"),
  username = casper.cli.get("username"),
  password = casper.cli.get("password"),
  emails = casper.cli.get("emails"),
  invitePage = 'https://' + subdomain + '.slack.com/admin/invites/full';

casper.start(invitePage, function() {
  this.fill('form[action="/"]', {
    'email': username,
    'password': password
  }, true);
});

casper.waitForUrl(invitePage, function() {
  this.click('a[onclick="TS.web.admin_invites.switchView(\'bulk\');"]');
});

casper.waitFor(function check() {
  return this.evaluate(function() {
    return document.querySelector('#bulk_invites').getAttribute('class') === '';
  });
  }, function then() {
    this.sendKeys('textarea[name=emails]', emails);
    this.click('div#bulk_invites .btn.btn-primary.small_right_margin.api_parse_emails.ladda-button.ladda');
});

casper.waitForText("We've done our best to guess", function() {
  this.click('.btn.btn-primary.small_bottom_margin.api_send_invites.ladda-button.full_width');
});

casper.waitForText("Invites sent!", function() {
  this.echo('Invites sent.');
});

casper.run();
