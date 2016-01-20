# Systémové požadavky #

  * JDK 6
  * J2EE aplikační server (servlet kontejner) - obvykle Apache Tomcat 6
  * přístup k databázi RD.CZ
  * databáze URNNBN - je podporován Oracle 9 nebo PostgreSQL 8.4 a vyšší


# Sestavení #

Aplikace je distribuována jako standardní soubor war, který je možno stáhnout ze sekce Downloads. War je také možno sestavit přímo ze zdrojového kódu v repository SVN pomocí nástroje Maven příkazem `mvn install`. Většina potřebných závislostí je běžně přístupná ve veřejných úložištích maven, s výjimkou jdbc ovladače Oracle, který je možno získat na stránkách `technet.oracle.com`, a jádra administrátorské aplikace, které je k dispozici na stránkách projektu [Aplikator](http://code.google.com/p/aplikator). Obě tyto závislosti je třeba nainstalovat do lokálního úložiště mavenu.

# Instalace #

URN:NBN resolver je standardní J2EE web aplikace, která se instaluje obvyklými prostředky použitého aplikačního serveru. V případě Apache Tomcat tedy nakopírováním `urnnbn.war` do adresáře `webapps`.

Dále je třeba definovat následující datasource v context.xml (samozřejmě s doplněním správných IP adres, názvů databází, uživatelů a hesel)
```
<Resource name="jdbc/aplikatorDS" auth="Container" 
              type="javax.sql.DataSource" driverClassName="oracle.jdbc.driver.OracleDriver"
              url="jdbc:oracle:thin:@//x.x.x.x:1521/nazev_databaze_URNNBN"
              username="xxx" password="xxx" maxActive="20" maxIdle="10"
              maxWait="-1"/>

    
    
<Resource name="jdbc/RDCZDS" auth="Container"
              type="javax.sql.DataSource" driverClassName="oracle.jdbc.driver.OracleDriver"
              url="jdbc:oracle:thin:@//x.x.x.x:1521/nazev_databaze_RDCZ"
              username="xxx" password="xxx" maxActive="20" maxIdle="10"
              maxWait="-1"/> 
```

V databázi URNNBN je třeba vytvořit tabulky schématu urnnbn spuštěním servletu na adrese `.../urnnbn/ddl?update=true` . Parametr `update` určuje, zda se příslušný DDL skript spustí (true) nebo jestli se pouze vypíše do odpovědi servletu (false).

Uživatelská nápověda, zobrazená v iframe uprostřed úvodní stránky vyhledávací aplikace resolveru, je samostatný website, přístupný na adrese `/urnnbnhelp` (česká verze) nebo `/urnnbnhelp/en` (anglická verze) na stejném serveru jako aplikace urnnbn. Obsah těchto stránek je zcela nezávislý a není součástí distribuce `urnnbn.war`.



# Správa uživatelských účtů #

Pro přístup do administrátorské části URN:NBN resolveru je třeba autentizace uživatele s rolí `urnnbn`. Autentizace uživatelů a jejich správa je ponechána na prostředcích aplikačního serveru. Pro Apache Tomcat je tedy třeba editovat soubor conf/tomcat-users.xml a přidat například takovýto blok:

```
<user username="spravce" password="heslo" roles="manager,urnnbn"/>
```