import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Klasa bazowa reprezentująca klienta.
 */
class Klient implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String imie;
    private String nazwisko;
    private double saldo;
    private double oprocentowanie; // np. w %

    public Klient(int id, String imie, String nazwisko, double saldo, double oprocentowanie) {
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.saldo = saldo;
        this.oprocentowanie = oprocentowanie;
    }

    public int getId() {
        return id;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getOprocentowanie() {
        return oprocentowanie;
    }

    public void setOprocentowanie(double oprocentowanie) {
        this.oprocentowanie = oprocentowanie;
    }

    /**
     * Metoda naliczająca standardowe oprocentowanie.
     * Jeżeli Klient nie jest VIP, oprocentowanie to pole {@code oprocentowanie}.
     */
    public void naliczOprocentowanie() {
        double noweSaldo = saldo + (saldo * (oprocentowanie / 100.0));
        setSaldo(noweSaldo);
    }

    @Override
    public String toString() {
        return String.format("Klient[ID=%d, %s %s, saldo=%.2f, oprocentowanie=%.2f%%]",
                id, imie, nazwisko, saldo, oprocentowanie);
    }
}

/**
 * Klient VIP z dodatkowym oprocentowaniem.
 */
class KlientVIP extends Klient {
    private static final long serialVersionUID = 1L;
    private double dodatkoweOprocentowanie; // np. w %

    public KlientVIP(int id, String imie, String nazwisko, double saldo,
                     double oprocentowanie, double dodatkoweOprocentowanie) {
        super(id, imie, nazwisko, saldo, oprocentowanie);
        this.dodatkoweOprocentowanie = dodatkoweOprocentowanie;
    }

    public double getDodatkoweOprocentowanie() {
        return dodatkoweOprocentowanie;
    }

    public void setDodatkoweOprocentowanie(double dodatkoweOprocentowanie) {
        this.dodatkoweOprocentowanie = dodatkoweOprocentowanie;
    }

    /**
     * Nadpisana metoda naliczająca dodatkowe oprocentowanie dla VIP.
     */
    @Override
    public void naliczOprocentowanie() {
        // Oprocentowanie = oprocentowanie z klasy bazowej + dodatkowe
        double baza = getOprocentowanie();
        double noweSaldo = getSaldo() + (getSaldo() * ((baza + dodatkoweOprocentowanie) / 100.0));
        setSaldo(noweSaldo);
    }

    @Override
    public String toString() {
        return String.format("KlientVIP[ID=%d, %s %s, saldo=%.2f, oprocentowanie=%.2f%%, dodatkowe=%.2f%%]",
                getId(), getImie(), getNazwisko(), getSaldo(), getOprocentowanie(), dodatkoweOprocentowanie);
    }
}

/**
 * Klasa zarządzająca listą klientów i operacjami na nich.
 */
class BankManager {
    private List<Klient> listaKlientow;

    public BankManager() {
        listaKlientow = new ArrayList<>();
    }

    /**
     * Wczytanie danych z pliku binarnego.
     */
    @SuppressWarnings("unchecked")
    public void wczytajZPliku(String nazwaPliku) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nazwaPliku))) {
            listaKlientow = (ArrayList<Klient>) ois.readObject();
            System.out.println("Dane wczytane z pliku: " + nazwaPliku);
        } catch (FileNotFoundException e) {
            // Plik może nie istnieć za pierwszym uruchomieniem
            System.out.println("Brak pliku z danymi. Utworzono nową listę klientów.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zapisanie danych do pliku binarnego.
     */
    public void zapiszDoPliku(String nazwaPliku) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nazwaPliku))) {
            oos.writeObject(listaKlientow);
            System.out.println("Dane zostały zapisane do pliku: " + nazwaPliku);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dodajKlienta(Klient klient) {
        // Prosta walidacja, by nie dodawać dwóch klientów o tym samym ID
        if (znajdzKlientaPoId(klient.getId()) != null) {
            System.out.println("Klient o podanym ID już istnieje!");
            return;
        }
        listaKlientow.add(klient);
        System.out.println("Dodano klienta: " + klient);
    }

    public Klient znajdzKlientaPoId(int id) {
        for (Klient k : listaKlientow) {
            if (k.getId() == id) {
                return k;
            }
        }
        return null;
    }

    public void zasilKonto(int id, double kwota) {
        Klient k = znajdzKlientaPoId(id);
        if (k != null) {
            if (kwota > 0) {
                k.setSaldo(k.getSaldo() + kwota);
                System.out.println("Zasilono konto ID=" + id + " kwotą: " + kwota);
            } else {
                System.out.println("Kwota musi być dodatnia!");
            }
        } else {
            System.out.println("Nie znaleziono klienta o ID=" + id);
        }
    }

    public void wyplataZKonta(int id, double kwota) {
        Klient k = znajdzKlientaPoId(id);
        if (k != null) {
            if (kwota > 0 && kwota <= k.getSaldo()) {
                k.setSaldo(k.getSaldo() - kwota);
                System.out.println("Wypłacono z konta ID=" + id + " kwotę: " + kwota);
            } else {
                System.out.println("Brak wystarczających środków lub podano niepoprawną kwotę!");
            }
        } else {
            System.out.println("Nie znaleziono klienta o ID=" + id);
        }
    }

    public void przelew(int idZrodlowy, int idDocelowy, double kwota) {
        if (idZrodlowy == idDocelowy) {
            System.out.println("Przelew na to samo konto jest niemożliwy!");
            return;
        }
        Klient kZrodlo = znajdzKlientaPoId(idZrodlowy);
        Klient kCel = znajdzKlientaPoId(idDocelowy);
        if (kZrodlo != null && kCel != null) {
            if (kwota > 0 && kwota <= kZrodlo.getSaldo()) {
                kZrodlo.setSaldo(kZrodlo.getSaldo() - kwota);
                kCel.setSaldo(kCel.getSaldo() + kwota);
                System.out.println("Wykonano przelew z ID=" + idZrodlowy +
                        " do ID=" + idDocelowy + " na kwotę: " + kwota);
            } else {
                System.out.println("Niewystarczające środki lub niepoprawna kwota!");
            }
        } else {
            System.out.println("Nie znaleziono kont źródłowego lub docelowego!");
        }
    }

    public void naliczOprocentowanie(int id) {
        Klient k = znajdzKlientaPoId(id);
        if (k != null) {
            k.naliczOprocentowanie();
            System.out.println("Naliczono oprocentowanie dla konta ID=" + id);
        } else {
            System.out.println("Nie znaleziono klienta o ID=" + id);
        }
    }

    public void wypiszWszystkichKlientow() {
        if (listaKlientow.isEmpty()) {
            System.out.println("Brak klientów w bazie.");
        } else {
            for (Klient k : listaKlientow) {
                System.out.println(k);
            }
        }
    }

    public void usunKlienta(int id) {
        Klient k = znajdzKlientaPoId(id);
        if (k != null) {
            listaKlientow.remove(k);
            System.out.println("Usunięto klienta o ID=" + id);
        } else {
            System.out.println("Nie znaleziono klienta o ID=" + id);
        }
    }
}

/**
 * Główna klasa z metodą main - prosta aplikacja konsolowa.
 */
public class BankApplication {

    private static final String PLIK_BAZY = "bazaKlientow.bin";

    public static void main(String[] args) {
        BankManager manager = new BankManager();

        // Wczytujemy istniejących klientów z pliku (o ile plik istnieje)
        manager.wczytajZPliku(PLIK_BAZY);

        Scanner sc = new Scanner(System.in);
        boolean dzialaj = true;

        while (dzialaj) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Dodaj nowego klienta");
            System.out.println("2. Dodaj nowego klienta VIP");
            System.out.println("3. Zasil konto");
            System.out.println("4. Wypłać z konta");
            System.out.println("5. Przelew między kontami");
            System.out.println("6. Nalicz oprocentowanie dla konta");
            System.out.println("7. Wyświetl listę wszystkich klientów");
            System.out.println("8. Wyszukaj klienta po ID");
            System.out.println("9. Usuń klienta po ID");
            System.out.println("0. Zakończ i zapisz dane");
            System.out.print("Wybierz opcję: ");

            int wybor = -1;
            try {
                wybor = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Niepoprawna opcja!");
            }

            switch (wybor) {
                case 1:
                    System.out.print("Podaj ID: ");
                    int id = Integer.parseInt(sc.nextLine());
                    System.out.print("Podaj imię: ");
                    String imie = sc.nextLine();
                    System.out.print("Podaj nazwisko: ");
                    String nazwisko = sc.nextLine();
                    System.out.print("Podaj saldo początkowe: ");
                    double saldo = Double.parseDouble(sc.nextLine());
                    System.out.print("Podaj oprocentowanie (w %): ");
                    double oprocentowanie = Double.parseDouble(sc.nextLine());

                    Klient nowy = new Klient(id, imie, nazwisko, saldo, oprocentowanie);
                    manager.dodajKlienta(nowy);
                    break;

                case 2:
                    System.out.print("Podaj ID: ");
                    int idVIP = Integer.parseInt(sc.nextLine());
                    System.out.print("Podaj imię: ");
                    String imieVIP = sc.nextLine();
                    System.out.print("Podaj nazwisko: ");
                    String nazwiskoVIP = sc.nextLine();
                    System.out.print("Podaj saldo początkowe: ");
                    double saldoVIP = Double.parseDouble(sc.nextLine());
                    System.out.print("Podaj oprocentowanie (w %): ");
                    double oprocentowanieVIP = Double.parseDouble(sc.nextLine());
                    System.out.print("Podaj dodatkowe oprocentowanie (w %): ");
                    double dodatkowe = Double.parseDouble(sc.nextLine());

                    KlientVIP vip = new KlientVIP(idVIP, imieVIP, nazwiskoVIP, saldoVIP,
                                                  oprocentowanieVIP, dodatkowe);
                    manager.dodajKlienta(vip);
                    break;

                case 3:
                    System.out.print("Podaj ID klienta: ");
                    int idZasil = Integer.parseInt(sc.nextLine());
                    System.out.print("Podaj kwotę zasilenia: ");
                    double kwotaZasilek = Double.parseDouble(sc.nextLine());
                    manager.zasilKonto(idZasil, kwotaZasilek);
                    break;

                case 4:
                    System.out.print("Podaj ID klienta: ");
                    int idWyplata = Integer.parseInt(sc.nextLine());
                    System.out.print("Podaj kwotę wypłaty: ");
                    double kwotaWyplata = Double.parseDouble(sc.nextLine());
                    manager.wyplataZKonta(idWyplata, kwotaWyplata);
                    break;

                case 5:
                    System.out.print("Podaj ID konta źródłowego: ");
                    int idZrodlo = Integer.parseInt(sc.nextLine());
                    System.out.print("Podaj ID konta docelowego: ");
                    int idCel = Integer.parseInt(sc.nextLine());
                    System.out.print("Podaj kwotę przelewu: ");
                    double kwotaPrzelew = Double.parseDouble(sc.nextLine());
                    manager.przelew(idZrodlo, idCel, kwotaPrzelew);
                    break;

                case 6:
                    System.out.print("Podaj ID klienta: ");
                    int idOproc = Integer.parseInt(sc.nextLine());
                    manager.naliczOprocentowanie(idOproc);
                    break;

                case 7:
                    manager.wypiszWszystkichKlientow();
                    break;

                case 8:
                    System.out.print("Podaj ID klienta: ");
                    int idSzukany = Integer.parseInt(sc.nextLine());
                    Klient znaleziony = manager.znajdzKlientaPoId(idSzukany);
                    if (znaleziony != null) {
                        System.out.println("Znaleziono: " + znaleziony);
                    } else {
                        System.out.println("Brak klienta o ID=" + idSzukany);
                    }
                    break;

                case 9:
                    System.out.print("Podaj ID klienta do usunięcia: ");
                    int idUsun = Integer.parseInt(sc.nextLine());
                    manager.usunKlienta(idUsun);
                    break;

                case 0:
                    // Zapisujemy stan bazy do pliku i kończymy
                    manager.zapiszDoPliku(PLIK_BAZY);
                    dzialaj = false;
                    break;

                default:
                    System.out.println("Nieznana opcja!");
                    break;
            }
        }

        sc.close();
        System.out.println("Zakończono działanie programu.");
    }
}

