var casper = require('casper').create({
  viewportSize: {width: 950, height: 950},
  pageSettings: {
    userAgent: 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36'
  }
});

casper.options.waitTimeout = 10000;

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
}, function() {
  this.evaluate(function(emails) {
    var method = 'users.admin.parseEmails',
        unixtime = Math.round(new Date().getTime() / 1000),
        url = TS.boot_data.api_url + method + "?t=" + unixtime,
        args = {
          emails: emails,
          set_active: 'true',
          token: TS.boot_data.api_token
        };
    TS.api.ajax_call(url, method, args, false, function(data) {
      data.emails.forEach(function(email) {
        var method = 'users.admin.invite',
            unixtime = Math.round(new Date().getTime() / 1000),
            url = TS.boot_data.api_url + method + "?t=" + unixtime,
            args = {
              email: email.email,
              first_name: email.first_name || '',
              last_name: email.last_name || '',
              set_active: 'true',
              token: TS.boot_data.api_token
            };
        TS.api.ajax_call(url, method, args, false, function(data) {
          if (data.ok || data.error == 'sent_recently' || data.error == 'already_invited') {
            document.getElementById('invite_sending_success').style.display = '';
          }
        });
      });
    });
  }, { emails: emails });
});

casper.waitUntilVisible('#invite_sending_success');

casper.run();
