# WaterPipes

## Vysvetlenie

Cieľom hry je prepojiť štart a cieľ pomocou správneho otočenia trubiek. Ak sa nám podarí cestu správne otočiť, po jej skontrolovaní chceme prejsť do ďalšieho levelu (nanovo vygenerovať hraciu plochu).
Štart a cieľ hry sa náhodne generuje, vždy na protiľahlej strane hernej plochy. Trubky sú po vygenerovaní cesty(cesta sa generuje pomocou DFS), náhodne otočené.

## Požiadavky

Hra je hrateľná pomocou myši, keď myšou prejdem ponad pole, dané pole sa zvýrazni. Ak sa na danom poli nachádza trubka, pomocou kliknutia myši ju viem otočiť.
V menu sa nachádza:
* informácia o tom, v ktorom som leveli, a veľkosť plochy.
* komponent pomocou ktorého je možné zmeniť veľkosť hracej plochy (iba na hodnoty 8 a vyššie). Pri zmene veľkosti vždy resetujem hru.
* tlačidlo, ktorým vieme hru zresetovať.
* tlačidlo, ktorým vieme skontrolovať správnosť našej cesty.

Stlačením klávesy R na klávesnici vieme tiež hru reštartovať, a pomocou klávesy ESC vypnúť, a pomocou tlačidla ENTER skontrolovať správnosť našej cesty.

Pri kontrole správnej cesty vyznačiť od štartu všetky správne otočené trubky až po prvú chybnú.


