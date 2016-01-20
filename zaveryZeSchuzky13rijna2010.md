## schuzka 13.10 a 26.10 ##

.NK
_přítomni: Jan Hutař, Ladislav Cubr, Pavel Kocourek, Vladimir Lahoda, Andrea Fojtů_

**níže připojuji zápis, jak jsme debatu pochopili v NK, rád bych, aby jsme na věc měli stejný pohled ;-) Pokud něco chybí, prosím doplnit/upravit.**


1. SLOUČENÍ POLÍ
- z duvodu zpetneho porovnávání záznamů nebudeme slučovat. Ve vyhledávání (výsledku) bude zobrazeno jedno nebo druhé podle dat.

2. SCHÉMATA
-zaslat nová schémata z poslední schůzky, kde jsou úpravy oproti schématům ze zadání
-naše kontrola vůči zadání, porovnání změn, případné doplnění


úpravy z 26.10

v tabulce int. entita - přidat pole:

**pro typ entity ročník > číslo ročníku; rok vydání**

**pro typ entity číslo> číslo ročníku, číslo "čísla"**

úprava u tabulky instituce - pole link změna počtu znaků z 50 na 200- je to url do báze ADR NK

![http://urnnbnresolver.googlecode.com/svn/wiki/DomainModel.jpg](http://urnnbnresolver.googlecode.com/svn/wiki/DomainModel.jpg)

3. vyhledávání – jednoduché (jedno dotazovací pole)
-nápis před oknem „trvalý identifikátor“
- výběr nebude pravděpodobně potřeba vzhledem k syntaxi identifikátorů. Zkusíme bez něj.
(URN:NBN,čČNB,ISBN,ISSN)
-nápověda navrhujeme zobrazit, dokud není zadán dotaz, a když se nic nenajde.

![http://urnnbnresolver.googlecode.com/svn/wiki/GUI.jpg](http://urnnbnresolver.googlecode.com/svn/wiki/GUI.jpg)

4. vyhledávání - rozšířené
- navrhujeme začlenit zobrazení bloku informací o identifikátorech přímo do záznamu v Registru.

5. způsob vyhledávání
-podle ISSN /CNBN mohou vylézt např. stovky čísel
-možnost výsledek seskupovat podle let / ročníků apod., abychom zachovali možnost hledání podle ISSN
Pouzijeme v návrhu komponentu Strom.

6. výsledek vyhledávání
-spojení obrazovek – výsledek i vyhledávač budou v jednom okně

7. syntax
-pouze šest polí, bez granularity za nimi
-primárně numerické znaky, později alfanumerické (celkem 2 miliardy kombinací)

8. propojení na další systémy
chystá se několik systémů, měli bychom alespoň být připraveni k integraci

ERDS (Europeana Resolution Discovery Service):
http://www.europeanaconnect.eu/documents/D5.4.1_eConnect_%20Europeana%20Resolution%20Service_v1.0_DNB.pdf

http://sourceforge.net/projects/metaresolver/

PERSID
http://www.persid.org/
-do konce roku chystají mezinárodní URN:NBN resolver spojující národní URN:NBN resolvery

**9. základní entity
-nové workflow (úprava RD-CZ?) - možnost rozsekávat vše na čísla, aby se URN:NBN dalo přidělovat jen číslům seriálů?
-můžeme akceptovat přidělování titulu, ročníku, číslu, ale nic mezi (půlka ročníku), ani nic níže (stránka)**
To je na "přidělujících" :)