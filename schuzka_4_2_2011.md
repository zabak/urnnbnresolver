# výstupy ze schůzky 4.2.2011 #
1)
**hodnoty záznamů v RD.cz v poli financování** - pro to, aby do resolveru šlo jen to co má - tj. data NK

  * hotovo, je potřeba dořešit tu dokumenty, kt. vznikly v NK a nejsou v ramci Norskych fondu ani VISK7 ani Povodni. Tam by se dalo je odlisit dle vlastnika dig. dokumentu, dat jim priznak 1 a do financovani dat napr. "NK rozpocet"

- už jsem začal zjišťovat


2) **opakované stahování polí** z RD.cz

- urcite ano pro pole URL
- pro ISSN a ISBN take - kdyby to delalo neplechu, tak uvidime co dal

  * hotovo - jak často?

3) **pridani poli do tabulky dig. reprezentace**
dale jsme se domluvili na tom, ze do tabulky digitalni reprezentace pridame pole Financovani a Cislo zakazky

  * hotovo - jen přehodit od IE do tabulky Dig. reprezentace

# nové #

4) **co nejdříve vytvořit dokumentaci a dát ji na wiki google code**

- popis aplikace a jejího nastavení

- popis admin rozhrani

- popis funkčnosti

- popis interakce s ostatními aplikacemi

5) **vyhledávání dle části řetězce**

- hotovo - problémy, když někdo hledá např. U omylem - počet hitů omezen na 500

6) **URL konstruované z URN pro odkazy z <> do katalogu**

- funguje např. resolver.nkp.cz/URN:NBN:ABA001:123456

7) **časovač na načtení nových dat z RD.cz - příprava**

- je potřeba udělat přípravu

8) **textové pole na hlavní stránku**

- je potřeba napsat na hlavní web krátce principy a zásady fungování a přidělování URN:NBN
- k tomu je potřeba mít možnost dát text do konfiguráku?do šablony?

**dodělat v rámci NK**

- nastavení serveru

- přesměrování na resolver.nkp.cz



---

16.2. doplněno vyhledávání

Dobrý den, je to skoro tak, jen za tím ?library=   musí být (libovolně dlouhý) začátek úplného URL, tedy včetně http://

Takže bude fungovat: http://sluzby.incad.cz/urnnbn/URN:NBN:CZ:ABA000:00171U?library=http://kramerius.nkp.cz




---

11.2 dodelano

Vyhledávání funguje tak, že se hledají všechny identifikátory, které začínají zadaným textem, case sensitive. Změním to na case insensitive a hledání výskytu zadaného textu na libovolném místě identifikátorů (také zítra). Uvidíme, jak se to osvědčí. Na stávajících 10 000 záznamech se to asi neprojeví, ale až tam bude víc dat, bude takové hledání uvnitř textu pomalé, protože nemůže použít index. A předpokládám, že si někteří uživatelé budou stěžovat, že při zadání příliš krátkého výrazu to nachází všechno možné, jen ne to, co měli na mysli ...