# inviteyoself

Allow people to invite themselves to your [Slack](https://slack.com) team.

This makes a website where users enter their email, and an invite will automatically be mailed to them from your Slack. Users can accept the invitation and join your Slack without needing you (the admin) to do any work.

**Note**: This is _not_ using the Slack API, it is mechanizing the website. So you have to give Heroku (or your own server) your actual admin email and password, not an API token.

## Important Note

This has only been tested with the free plan, but it _should_ work with a paid plan too. If it doesn't, please [file a ticket](https://github.com/seabre/inviteyoself/issues).

## Installation

This was setup for [Heroku](https://www.heroku.com), so that is the suggested platform. Otherwise, you will need to provide leiningen, postgres, and casperjs yourself.

* Install [leiningen](https://github.com/technomancy/leiningen) using its install script (don't use `apt`).
* Clone this project and `cd` into its directory..
* Add an application to Heroku.
* Add a Heroku git remote to this project:

```bash
heroku git:remote -a yourherokuapp`
```

* Add [Postgres](https://www.heroku.com/postgres) to your application:

```bash
heroku addons:add heroku-postgresql:dev
```

* Add [Heroku scheduler](https://scheduler.heroku.com) to your application:

```bash
heroku addons:add scheduler
```

* Set the [environment variables](#environment-variables) for the application, substituting your own values for those shown. Start with the Slack details:

```bash
heroku config:add SLACK_SUBDOMAIN=yourteam
heroku config:add SLACK_USERNAME=youremail
heroku config:add SLACK_PASSWORD=yourpassword
```

* Then set your Postgres details. Find them by visiting your app's Resources tab in Heroku, then clicking the "Heroku Postgres :: Aqua" link. This should take you to a page with your assigned Postgres DB settings.

```bash
heroku config:add DB_HOST=yourhost
heroku config:add DB_NAME=yourdatabase
heroku config:add DB_USER_NAME=youruser
heroku config:add DB_USER_PASSWORD=yourpassword
```

* Finally, set the buildpack URL to a specific URL:

```bash
heroku config:add BUILDPACK_URL=https://github.com/ddollar/heroku-buildpack-multi.git
```

* Run the migrations (**locally**). Substitute the all-caps variables below for the same values you set on your Heroku server in the previous step.

```bash
lein ragtime migrate -d "jdbc:postgresql://DB_HOST:5432/DB_NAME?user=DB_USER_NAME&password=DB_USER_PASSWORD&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
```

* Visit [Heroku scheduler](https://scheduler.heroku.com) in your browser and add the following task to run every 10 minutes: `lein send-invites`

* Push to Heroku to deploy the app:

```bash
git push heroku master
```

* Visit the app in the browser at `https://yourappname.herokuapp.com` and give it a shot! Test it with something other than your own admin email, of course.

### Environment Variables

To set an environment variable, run `heroku config:add NAME_OF_VARIABLE=SOMEVALUE` in the root of your project.

* `SLACK_SUBDOMAIN`
  * The subdomain for your Slack team. e.g. If your Slack team url is `https://myslackteam.slack.com`, your subdomain is `myslackteam`.
* `SLACK_USERNAME`
  * The username for an admin user on the Slack team. Should be an email address.
* `SLACK_PASSWORD`
  * The password for the `SLACK_USERNAME`.
* `DB_HOST`
  * The postgres host.
* `DB_NAME`
  * The name of the database in Postgres that the application will be using.
* `DB_USER_NAME`
  * The postgres database user to access `DB_NAME`.
* `DB_USER_PASSWORD`
  * The password for `DB_USER_NAME`.
* `BUILDPACK_URL`
  * This should always be: `https://github.com/ddollar/heroku-buildpack-multi.git`

## Local development

If you're developing this app locally, you **will** need the following: casperjs, postgres, leiningen, java (OpenJDK 6 and 7 both work.)

### Styles

I'm using compass to compile sass. Run `compass watch` from `resources/vendor/` and edit the sass files in the sass folder.

## Environment Variables

In project.clj, look for `{:dev true :db-user-name "postgres" :db-user-password "password" :db-host "localhost" :db-name "inviteyoself"}`, these correspond to the environment variables above. Add the correct keys and values for your system.

## Running

To start a web server for the application, run: `lein ring server`.

If you want to run the invite runner, run: `lein send-invites`.

## License

Under the [MIT License](LICENSE).
