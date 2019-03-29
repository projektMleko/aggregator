# Password Ecnryption
Jako, że nie chcemy trzymać haseł, loginów i innych kluczy API czystym tekstem, prosząc się o kłopoty... 
Zastosowałem dosyć prostackie rozwiązanie polegające na kodowaniu wrażliwych informacji jascryptem przy użyciu klucza.

## Ustawienie klucza
Żeby aplikacja mogła poprawnie zdekodować wrażliwe informacje do postaci nadającej się do użytku, należy dodać zmienną środowiskową **AGGREGATOR_SECRET_KEY**.
Wartość tej zmiennej systemowej to oczywiście klucz, przy pomocy którego rozkodowane zostają wszystkie wrażliwe dane. **Klucz podam tylko członkom projektu :D**