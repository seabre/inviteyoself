# inviteyoself

Allow people to invite themselves to your slack team.

## Important Note

This has only been tested with the free plan. I don't have access to a paid one. If it works for you, let me know.

## Installation

This was setup for heroku, so that is the suggested platform. Otherwise, you will need to provide leiningen, postgres, and casperjs yourself.

1. Install [leiningen](https://github.com/technomancy/leiningen).
2. Clone this project.
3. Add an application to heroku.
4. Add a heroku git remote to this project: `heroku git:remote -a thenameofyourherokuapplication`
5. Add postgres to your application.
6. Add heroku scheduler to your application.
7. Set the environment variables for the application. See below for more details.
8. Run the migrations, preferably locally: `lein ragtime migrate -d "jdbc:postgresql://MY_DATABASE_HOST:5432/MY_DATABASE_NAME?user=MY_DATABASE_USER&password=MY_DATABASE_PASSWORD&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"``
9. Add the following task to run every 10 minutes in heroku scheduler: `lein send-invites`
10. Push to heroku: `git push heroku master`.

### Environment Variables

To set an environment variable, run `heroku config:add NAME_OF_VARIABLE=SOMEVALUE` in the root of your project.

* `SLACK_SUBDOMAIN`
  * The subdomain for your slack team. e.g. If your slack team url is http://myslackteam.slack.com, your subdomain is myslackteam.
* `SLACK_USERNAME`
  * The username for an admin user on the slack team. Should be an e-mail address.
* `SLACK_PASSWORD`
  * The password for the `SLACK_USERNAME`.
* `DB_HOST`
  * The postgres host.
* `DB_NAME`
  * The name of the table in postgres that the application will be using.
* `DB_USER_NAME`
  * The postgres database user to access `DB_NAME`.
* `DB_USER_PASSWORD`
  * The password for `DB_USER_NAME`.
* `BUILDPACK_URL`
  * This should always be: `https://github.com/ddollar/heroku-buildpack-multi.git`

## Local development

You *will* need the following: casperjs, postgres, leiningen, java (OpenJDK 6 and 7 both work.)

### Styles

I'm using compass to compile sass. Run `compass watch` from `resources/vendor/` and edit the sass files in the sass folder.

## Environment Variables

In project.clj, look for `{:dev true :db-user-name "postgres" :db-user-password "password" :db-host "localhost" :db-name "inviteyoself"}`, these correspond to the environment variables above. Add the correct keys and values for your system.

## Running

To start a web server for the application, run: `lein ring server`.

If you want to run the invite runner, run: `lein send-invites`.

## License

Under the MIT license. See LICENSE for details.
