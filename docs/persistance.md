# Persistance
W celu nauki pracy z bazami danych, skonfigurowałem baze danych, pozwalającą na operacje CRUD(Create, Read, Update, Delete).

## H2
H2 to lekka baza zaszyta w projekcie służąca jedynie do celów testowych. Można się do niej dostać za pomocą ścieżki:
> localhost:15001/h2

Zabezpieczenie powinno na początku przekierować nas do strony logowania, gdzie zalogujemy się poświadczeniami:
> username: user

> password: (co uruchomienie generuje się nowy i zapisany jest w logach)

Po zalogowaniu pojawi się konsola logowania do bazy. W tym miejscu nic nie wpisujemy i nie nie zmieniamy, jedynie klikamy **connect**

W konsoli mamy dostęp do wszystkich tabel które znajdują się w bazie wraz z możliwością manipulacji bazą przy użyciu konsoli SQL.


## Oracle Database
Faktyczna baza danych, która będzie używana na serwerze i będzie zawierała faktyczne dane na których operować będzie projekt.


## Testowanie
Dla celów testowych stworzyłem prostą strukturę składającą się z dwóch tabel:
- TEST (ID, DESCRIPTION, NUM_VALUE, STR_VALUE, TEST_CONTAINER_ID)
- TEST_CONTAINER (ID, CONTAINER_NAME)

Jako, że jest to **baza relacyjna**, to Tabele powiązałem w następujący sposób:
```
 ___________
|           |                 |->[TEST]
|    TEST   | --(one-to-many)--> [TEST]
| CONTAINER |                 |->[TEST]
|___________|                 |->[TEST]
```

Czyli wiele wierszy w tabeli TEST może być przypisanych do jednego wiersza w tabeli TEST CONTAINER.
Jest to relacja jeden do wielu. Coś jak relacja piłkarza z zespołem. Piłkarz ma przynależność do jednego zespołu, zespół ma wielu piłkarzy.

To, do jakiego TEST CONTAINER należy TEST zależy od wartości w kolumnie TEST_CONTAINER_ID, która określa jego przynależnoźć do TEST CONTRAINER.

### Praktyka
Żeby przetestować jak to działa w praktyce, stworzyłem dwie podstrony:
> localhost:15001/persistance/test?id=1

gdzie "id=1" po znaku równości możemy wpisać takie ID jakie nas interesuje. Strona wyświetli nam jeden wiersz z tabeli TEST

>localhost:15001/persistance/container?id=1

Strona wyświetli nam TEST CONTAINER i przynależne do niego wiersze z TEST. Relacja jeden-do-wielu.

Oba widoki wyświetlające dane znajdują się pod plikami html: test.html i test_container.html. 