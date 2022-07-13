# ChatSphere - Instant Messaging Platform

## Build

To compile the project you need `jdk10-openjdk` and `nodejs`, of course also the packet managers `maven` and `yarn`. Of course you need `git`. Finally, a MySQL database server is required, in our case `mariadb`. Simply under Arch-Linux `pacman -S git jdk10-openjdk nodejs yarn maven mariadb`. A BounceCastleProvider (e.g. `libbcprov-java` debian package) is required for push notifications functionallity.

You can check our `.gitlab-ci.yml`, which controls the automatic delivery to our live server.

### Compile

Basically, the server is compiled with `mvn package` to a Java Servlet whose *.war* file can be executed by the [Jetty HTTP-Server](https://www.eclipse.org/jetty/).
Again, the client is created by `yarn run build` in the `dist` subfolder, which can be delivered by a simple web server like [CaddyServer](https://caddyserver.com/).

If you have problems compiling with Maven regarding the JNA version, you can set the JVM argument `-Djna.nosys=true` in the `MAVEN_OPTS` environment variable. 

## Frontend QuickStart

```
$ cd ./client
$ yarn global add vue-cli ngrok
$ yarn install
$ yarn run dev
$ ngrok http -region=eu localhost:8080
```

### Frontend Tests

Die Umgebungsvariablen FIREFOX_BIN und CHROMIUM_BIN geben den Pfad zu FireFox 60+ und Google Chrome / Chromium 65+ an, um die Tests auszuführen. (Benötigt wird ES6 / ES2017 Support).
Das setzen der Variablen ist bei Abweichung zu Standardinstallationsordner notwendig.

## API QuickStart

You can use [GraphQL Launchpad](https://launchpad.graphql.com/) for Development and start a tiny local GraphQL SDL API Mock.

```
$ cd ./api
$ yarn install
$ yarn start
```

## Backend QuickStart

```
$ mvn
```
