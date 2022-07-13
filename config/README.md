# Setup
Diese Anleitung gilt für linux mit systemd.

Im Voraus sollte ein nicht-privilegierter Nutzer (in unserem Fall `chatsphere`) angelegt werden, mit dem die Services ausgeführt werden können.

## Caddy (HTTP/Proxy)
Der [Caddy-Server](https://caddyserver.com/) und zugehörige Service kann der [Standard-Anleitung](https://github.com/mholt/caddy/tree/master/dist/init/linux-systemd) entsprechend aufgesetzt werden. Um das Google QUIC/UDP-Protocol nutzen zu können wird zudem empfohlen `-quic` in der `caddy.service` standardmäßig als Argument zu übergeben. Als Beispiel und Referenz kann die Datei `caddy.service` herangezogen werden.

Die `Caddyfile` kann aus diesem Repository übernommen werden, jedoch sollten die Domainnamen und Einstellungen für TLS-Zertifikate und Security-Header angepasst werden.

### PWA (Client)

Die von YARN in `client` erzeugte Progressive Web Application aus dem Unterordner `dist` ist im WebRoot-Ordner des Caddy HTTP-Servers zu hinterlegen. Danach kann über die in Caddy konfigurierte Domain die PWA geladen werden.

## Jetty (Backend)
[Jetty](https://www.eclipse.org/jetty/) kann ebenfalls mit der [Standard-Anleitung](https://www.eclipse.org/jetty/documentation/current/quick-start-getting-started.html) installiert werden. Als `$JETTY_HOME` wird `/opt/jetty` empfohlen.

### Jetty-Konfiguration

Die minimal nötigen Module für den Chatsphere-Server kann man aktivieren, indem man den `start.d`-Ordner von jetty nach `$JETTY_BASE` kopiert.

Falls man sicherstellen möchte, die aktuelle Modul-Konfiguration zu verwenden, kann man die jetty-Modul-Konfigurationen auf folgende Weise generieren:
```bash
cd $JETTY_BASE
java -jar $JETTY_HOME/start.jar --add-to-start=http --add-to-start=deploy --add-to-start=websocket
```
Zusätzlich muss dann auch in der `http.ini` der `jetty.http.port` auf `6000` gesetzt und in der `deploy.ini` die Zeile `jetty.deploy.monitoredDir=webapps` aktiviert werden.

Um Jetty als Service nutzen zu können muss das Startskript in das init.d-Verzeichnis kopiert werden. systemd hat Integration von init.d, daher ist keine weitere Konfiguration des Services nötig und man kann systemctl sofort nutzen um Jetty zu starten.
```bash
cp $JETTY_HOME/bin/jetty.sh /etc/init.d/jetty
```

### Chatsphere-Servlet

Um das Chatsphere-Servlet ausführen zu können müssen vorher die Datenbank-Tabellen ausgeführt werden. Dazu muss die Datei `chatsphere_schema.sql`, die im `database`-Ordner liegt, in die Datenbank importiert werden:
```sql
USE chatsphere;
SOURCE chatsphere_schema.sql;
```

Das von Maven im `server`-Ordner erzeugt Servlet ist unter `$JETTY_BASE/webapp/root.war` zu speichern und wird bei korrekter Konfiguration von Jetty automatisch ausgeführt und kann im Webbrowser abgerufen werden. Da das Servlet nur eine Websocket-Verbindung bietet, ist es empfehlenswert, die Funktionalität über das Aufrufen des Clients zu testen.

## ORY-Hydra (oAuth2 Authorization-Server)
Nach der Installation der ORY-Hydra Binary muss die SQL-Datenbank einmalig initialisiert werden. Im Falle `localhost` als Host des Datenbankserver wegen der Go-URL-Syntax mit folgendem Befehl:
```bash
ory-hydra migrate sql mysql://user:password@/database
```

Für die Installation als Service muss die Datei `ory-hydra.service` ins `/etc/systemd/system/`-Verzeichnis kopiert werden. Nach einem neuladen der Dienste durch `systemctl daemon-reload` kann dieser später aktiviert und ausgeführt werden.

Die Konfigurationsdatei `.hydra.yml` muss in das Working-Directory des Services gelegt werden und die Datenbankzugangsdaten geändert sowie die Secret-Variablen gesetzt werden. Der Pfad zum Working-Directory muss in der Service-Datei angepasst werden.

Im `database`-Ordner liegt ein Datenbankdump `hyra.sql`, welcher in die Datenbank importiert werden kann um die Einstellungen wiederhzustellen.
```sql
USE chatsphere;
SOURCE hydra.sql;
```

## GraphQL-Documentation (Optional)
Für die Bereitstellung der Dokumentation muss die Datei `graphql-docs-server.service` ins `/etc/systemd/system/`-Verzeichnis kopiert werden. Nach dem Neuladen der Dienste durch `systemctl daemon-reload` kann der Service später ausgeführt werden.

Vor dem Starten ist es erforderlich, dass im Working-Directory die Dateien des Ordners `api` bereitgestellt werden.

# Dauerbetrieb
Um im Dauerbetrieb alle Dienste sicher bereitstellen zu können sollten alle Services über systemd aktiviert werden, dadurch werden sie nach einem Serverneustart automatisch mit dem System gestartet:
```bash
systemctl enable caddy.service
systemctl enable jetty.service
systemctl enable ory-hydra.service
systemctl enable graphql-docs-server.service
```
