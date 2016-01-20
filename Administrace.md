# Administrace #

Kliknutím na odkaz Administrace v levém dolním rohu vyhledávací obrazovky resolveru otevřete v novém okně administrátorskou aplikaci pro správu záznamů v resolveru. Pro přístup do aplikace se je třeba přihlásit jako uživatel s rolí `urnnbn`.

![http://urnnbnresolver.googlecode.com/svn/wiki/AdminGUITable.jpg](http://urnnbnresolver.googlecode.com/svn/wiki/AdminGUITable.jpg)

  1. _Přepínání tabulek_ Digitální knihovna a Instituce jsou číselníky, používané v základních tabulkách Intelektuálních entit a Digitálních reprezentací (viz [Datová struktura](UpravaDatoveStruktury.md)).
  1. _Spouštění dávkových funkcí_
    * `Načíst data` - načte všechny záznamy z tabulky Predloha v databázi RD.CZ, které mají ve sloupci Mainflag hodnotu 1. Pro každý takový záznam jsou vytvořeny odpovídající záznamy v tabulkách Intelektuální Entita, Digitální reprezentace a Zveřejněno. V původním záznamu v RD.CZ je hodnota ve sloupci Mainflag změněna na 2.
    * `PriraditURNNBN` - u všech záznamů v tabulce Digitální reprezentace, které mají prázdnou hodnotu ve sloupci URNNBN, je tato hodnota vygenerována a záznam je označen jako Aktivní.
  1. _Stránkování v tabulce_ - ikonky reprezentují(zleva) : první stránku, předchozí stránku, následující stránku, poslední stránku
  1. _Operace s vybraným záznamem_ - kliknutím na příslušný záznam jej v tabulce označíte (pokud je třeba) a vyberte ikonku požadované operace (zleva): nový záznam, editace záznamu, smazání záznamu, kopie záznamu, znovunačtení tabulky
  1. _Vyhledávání_ - záznamy v tabulce je možno setřídit kliknutím na záhlaví příslušného sloupce (opakovaným kliknutím se změní směr třídění). Ve sloupci, podle kterého je setříděno, je pak možno pomocí tohoto pole vyhledávat. Zadáním textu a kliknutím na ikonku zobrazíte záznamy počínající záznamem, který odpovídá zadanému textu.

Kliknutím na tlačítko editace (nebo dvojklikem na požadovaném záznamu) je vybraný záznam zobrazen v režimu formuláře:

![http://urnnbnresolver.googlecode.com/svn/wiki/AdminGUIForm.jpg](http://urnnbnresolver.googlecode.com/svn/wiki/AdminGUIForm.jpg)

  1. _Listování a operace se záznamy_ - obdobná funkcionalita jako v zobrazení tabulky
  1. _Zrušení/Uložení editace_ - levá z dvojice ikonek ukončí editaci a přejde do zobrazení tabulky, pravá ikonka uloží provedené změny ve formuláři
  1. _Aktivace záznamu_ - zaškrtnutím políčka je daný záznam aktivován a zahrnut do výsledků vyhledávání
  1. _`PriraditUrnnbn`_ - stejná funkcionalita, jako u dávkové funkce `PriraditURNNBN`, ale pouze pro aktuální záznam
  1. _Přidat opakování_ - kliknutím na ikonku + je přidán jeden záznam do "vnořené tabulky" v rámci daného formuláře
  1. _Odebrat opakování_ - kliknutím na křížek je daný záznam z vnořené tabulky odebrán
  1. _Výběr hodnoty z číselníku_ - zapsáním úvodních písmen z hledaného textu a výběrem položky z "našeptávacího" menu vyberete do této části formuláře odkazovanou položku z příslušného číselníku. Alternativně nemusíte zadávat hledaný text, ale kliknutím na ikonku lupy zobrazíte celou tabulku číselníku v modálním okně, kde můžete potřebný záznam vybrat a případně ho tam i do číselníku přidat.