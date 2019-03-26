# Flow prac z Git
Każda zmiana w projekcie powinna przechodzić przez ten sam proces, dzięki czemu łatwiej będzie poprawić i wyszukać pewne elementy w historii projektu.

## Podział zmian w kodzie 
Zmiany w ramach projektu dzielimy na poszczególne kategorie(prefixy):
- fix: poprawka, którą wdrażamy w momencie gdy aplikacja błędnie działa,
- feature: nowa funkcjonalność,
- refactor: poprawka kodu, która nie poprawia ani nie dodaje funkcjonalności, jedynie poprawia widoczność kodu albo skalowalność rozwiazania.

## Nazewnictwo
Branch nazywamy, tak by pasował do maski: prefix/nazwa_brancha

## Sposób wprowadzania zmian
Żeby wprowadzić zmiany w projekcie, przechodzimy poszczególne kroki
1. Przechodzimy na główny branch na który wprowadzamy zmiany. (czyli master)
2. Tworzymy nowy branch na bazie mastera. Będąc na masterze możemy zrobić to na dwa sposoby:
    3. git branch **nazwa brancha**, git checkout **nazwa brancha**
    4. git checkout -b **nazwa brancha**
5. Drugi sposób tworzy branch i odrazu się na niego przełącza, czyli jest połączeniem dwóch poprzednich komend.
6. Wprowadzamy zmiany w kodzie
7. Dodajemy zedytowane pliki do gita (git add)
8. Commitujemy zmiany z odpowiednim komentarzem (najlepiej opisującym co się zrobiło)
9. Pushujemy zmiany na serwer
10. Wchodzimy na github w zakładkę pull requests
11. Klikamy zielony przycisk New Pull Request
12. w base: master, compare: nasz branch  
13. W Assignee wybieramy osobe, która ma sprawdzić brancha (**NIE WYZNACZAMY SIEBIE**)
14. Jeżeli osoba sprawdzająca uzna, że wszystko jest OK, to klikamy **squash and merge**.
    