věci, které zatím chybí oproti zadání + další věci k debatě.

## 1. URN:NBN mají i dokumenty, které jsou mimo NK, VISK7 a Norské fondy(zadání str. 2) ##

nevíme, zda nejsou přidělena URN:NBN dokumentům, které nemá NK ve svém úložišti - tj. dokumenty, kt. vznikly mimo digitalizaci NK, VISK7 a Norské fondy.

_nelze to poznat, je tento údaj v RD.cz? jak zařídit, aby když přijde do RD.cz dokument, kt. si někdo udělal na koleně v Horní Dolní za svoje peníze, nedostal URN:NBN?_

_je nutné z resolveru takové dokumenty smazat, pokud tam jsou_

## 2. vyhledávání podle části řetězce (odsouhlaseno na poslední schůzce) ##

např. když jsou jako číslo ISSN v poli dvě čísla, potřebujeme možnost, aby to hledalo podle dvou čísel

## 3. změnit příklady pro vyhledávání ##

příklad - “ISSN (např.: 1802-6265, 0862-5921 ) je nestandardní, neměli bychom ukazovat, že některé dokumenty mohou mít 2 ISSN

možnost vrátit se na úvodní stránku s příklady k hledání

## 4. funkce přesměrování (zadání str. 5-6) ##

“po kliknutí na URN:NBN v katalogu možnost přímo zobrazit objekt v nějaké
vybrané lokaci (v katalogu jsou již metadata, nemá smysl odkazovat na další
metadatovou stránku)
tj. server identifikuje IP uživatele, který sedí v té konkrétní knihovně a vrátí mu přímo jen dostupnou kopii, tj. tu, kterou ta knihovna má ve sbírce a může ji v budově zpřístupnit; v případě, že žádná kopie není volně dostupná danému uživateli- vrátí metadatovou stránku”

_i když zatím v katalogu není URN:NBN, měli bychom se pobavit jak na to připravit resolver_

Online resolving při zadaní url obsahujícího urn.nbn (www.resolver.nkp.cz/urn:nbn-aba0001-123456L)


# k debatě  > #

## 5. poznámky k data modelu ##

Intelektualni Entita (popis fyzické jednotky) -
  * Co se stane, když budeme chtít přidat více čísel periodika? tj. bude více entit čísel pro jeden titul a všechna pole kromě čísla periodika se budou opakovat?
  * Není tam popis/identifikace vlastníka fyzického dokumentu - je v RD.cz?
  * nutno doplnit v rámci jakého projektu je digitalizováno! je v RD.cz?

Entita má jednu nebo více digitálních reprezentací, každá reprezentace má jen jednu Instituci (vlastník?) a více "zveřejněno"
  * může být u "zveřejněno" více digitálních knihoven vázaných na jednu instituci? mělo by to jít

## 6. drobnosti ##

Co když někdo v rd.cz změní url, dozvi se to resolver dnes?
jak se do RD.cz dostávají URL - zachycují se někde změny?

##  ##

## 7. další rozvoj ##

**_Nová digitalizace_**

  * Workflow pro vstup dat z nové digitalizace – vymyslet jak se dostanou URN:NBN přidělená během digitalizace (nebo obecně externě - Sirius, Transf. modul) do resolveru + vymyslet, jak se k nim dostane URL

  * Workflow pro vstup dat z nové externí digitalizace – pro případy, kdy se i zakládá nový dokument v rd.cz – podle mě ideálně při jedné operaci přidělit záznam v rd.cz a rovnou nahrát do resolveru, přidělit neaktivní identifikátory pro dokument. Pak ve druhé fázi, pokud bude mít zájem, připojit k číslu zakázky čísla – do rd.cz a pak do revolveru – zde by data mohla přijít už rovnou s URL…?

**_Stávající data_**

Jak dostat URN:NBN a technická metadata k nasim existujícím datům a do k4 a URL z  K4 do resolveru, na úroven čísel?

1) verze
  * v rámci přípravy dat pro ingest do nového LTP
  * manualne nebo poloautomaticky vzit DTD nasich balíků
  * importovat ke kazdé zakázce do RD.CZ is čísla a techMD
  * natáhnout všechny issues do resolveru jako entity a digiátlní dokumenty
  * přidělit jim urn.nbn
  * ke každé zákázce exportovat z resolveru jedno xml s identifikaci vsech issues + pridelenými urn.nbn
  * to pak použit pri ingestu do LTP
  * resolver zná URL odpovídající ročníku v K4, získá z K4 URL všech podřízených issue...?

2) verze -
  * resolver přidělí urn nbn tomu, co máme v RD.CZ (pro data z visku a naše data)
  * resolver zná URL odpovídající ročníku v K4, získá z K4 URL všech podřízených issue a založí je jako entity v resolveru...?
  * seznam issues + urn nbn ke každému číslu zakázky vyexportujeme do xml
  * při ingestu archivu do LTP vezeme urn nbn z tohoto xml

3) verze
  * resolver přidělí urn nbn tomu, co máme v RD.CZ (pro data z visku a naše data)
  * pri ingestu vygenerujeme v LTP systému (trans modulu) urn nbn pro každé issue
  * každé issue tak pujde do LTP se dvema urn nbn v metadatech: jedno pro issue jedno pro nadřízenou entitu (číslo zakázky, ročník)
  * z LTP  nejak musme vyexportovat seznam issues, jejich techMD a pridelena urn nbn a dostat do resolveru (oai? nebo xml csv rovnou do databáze?)
  * resolver pak musi nejak ziskat odpovidajici URL z k4:-)


**_Editor_**

asi bude třeba mít nějaký metadata editor – nějaký interface, který bude umožňovat dělat z ročníků periodik issues, a umět identifikátory urn.nbn issue odeslat do K4, také funkce jako přidávání polí metadat, nastavení profilů oai, nastavení zdrojů sklízení


**_Vazby na K4_**

K4 musí
  * Obohatit metadata čísel v K4 o urn nbn jak? (přes OAI-PMH)
  * Odeslat nebo vystavit metadata z k4 tak, aby resolver uměl sklidit nově přicházející dokumenty (titles, ročniky, issues) a přidělit identifikátory
  * Získat url pro data, která už v K4 budou mít identifikátor (IOP)