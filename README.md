# BankApplication
BankApplication
Opis projektu

BankApplication to prosta, konsolowa aplikacja bankowa w języku Java, która umożliwia przechowywanie informacji o klientach (w tym o klientach VIP), zarządzanie stanem ich kont oraz wykonywanie podstawowych operacji takich jak:

    Dodawanie nowych klientów (w tym VIP)
    Zasilenie konta
    Wypłata z konta (z ograniczeniem do wysokości dostępnego salda)
    Przelew pomiędzy dwoma kontami
    Naliczenie oprocentowania (również z uwzględnieniem dodatkowego oprocentowania klientów VIP)
    Wyświetlanie listy wszystkich klientów
    Wyszukiwanie klienta po ID
    (Opcjonalnie) Usuwanie klientów z bazy

Dane są przechowywane w pliku binarnym (bazaKlientow.bin) przy użyciu mechanizmu serializacji, dzięki czemu pozostają dostępne po ponownym uruchomieniu aplikacji.
Wymagania

    Java 8 (lub nowsza)
    System operacyjny z zainstalowaną Javą (Windows, Linux, macOS)

Uruchomienie

    Sklonuj repozytorium lub pobierz je w formie archiwum .zip:

git clone https://github.com/<TwojaNazwaUzytkownika>/BankApplication.git

Przejdź do katalogu projektu:

cd BankApplication

Skompiluj plik BankApplication.java:

javac BankApplication.java

Uruchom aplikację:

    java BankApplication

    Po uruchomieniu zobaczysz menu w konsoli, które pozwala wykonywać opisane operacje.

Funkcjonalności

    Dodawanie nowego klienta (zwykłego lub VIP) – wymaga podania ID, imienia, nazwiska, salda oraz oprocentowania (w %).
        W przypadku VIP także dodatkowego oprocentowania.
    Zasilenie konta – zwiększenie salda o podaną kwotę.
    Wypłata z konta – zmniejszenie salda o podaną kwotę (do maksymalnej wysokości salda).
    Przelew między kontami – jednoczesne zmniejszenie salda na jednym koncie i zwiększenie na drugim (tylko jeżeli kwota jest dostępna na koncie źródłowym).
    Naliczenie oprocentowania – obliczenie i dopisanie odsetek do stanu konta (dla klienta VIP uwzględniane jest oprocentowanie bazowe + dodatkowe).
    Wyświetlanie listy wszystkich klientów – wypisuje w konsoli wszystkich klientów z ich danymi (ID, imię, nazwisko, saldo).
    Wyszukiwanie klienta po ID – wyświetla dane pojedynczego klienta.
    Usuwanie klienta (opcjonalne) – usuwa klienta z bazy na podstawie ID.

Struktura plików

    BankApplication.java – zawiera:
        Klasy: Klient (bazowa), KlientVIP (dziedzicząca po Klient),
        Klasę BankManager, zarządzającą listą klientów i operacjami na ich kontach,
        Metodę main, która w pętli obsługuje konsolowe menu programu.

    bazaKlientow.bin – plik binarny zawierający serializowane obiekty klientów. Jeżeli plik nie istnieje, aplikacja utworzy go podczas zapisywania stanu przy zamykaniu.

Możliwe rozszerzenia

    Rozszerzenie systemu o bazę danych (np. SQLite, MySQL), zamiast pliku binarnego.
    Dodanie historii transakcji każdego klienta (np. do osobnego pliku lub tabeli w bazie).
    Wprowadzenie różnych typów kont (np. oszczędnościowe, inwestycyjne) z różnymi zasadami naliczania oprocentowania.
    Rozbudowa warstwy interfejsu graficznego (GUI w JavaFX lub Swing).
    Testy jednostkowe (JUnit) sprawdzające poprawność poszczególnych funkcjonalności.

Licencja

Projekt może być używany i modyfikowany zgodnie z potrzebami. Możesz dodać własną licencję, np. MIT lub Apache.

Autor: [Łukasz Ziemiański / Ohlauer]
