# MoneyManager - Twój Osobisty Tracker Wydatków
Aplikacja MoneyManager służy do zarządzania finansami osobistymi i działa jako prosty system do zapisywania przychodów oraz wydatków, umożliwia planowania przyszłych płatności i daje możliwość śledzenia bilansu na wykresach.

Użytkownik najpierw zakłada konto lub się loguje za pomocą adresu e-mail i PIN-u. Po zalogowaniu trafia na ekran główny, gdzie widzi swoje saldo, przychody, wydatki oraz ostatnie transakcje. 
Może dodawać nowe operacje finansowe, które są zapisywane w lokalnej bazie danych (SQLite przez Room). 
Każda transakcja jest przypisana do konkretnego użytkownika przez `userId`, dzięki czemu dane różnych użytkowników są od siebie oddzielone. 
Aplikacja automatycznie aktualizuje saldo i listę transakcji po każdej zmianie. Dodatkowo użytkownik może przeglądać historię operacji oraz zarządzać zaplanowanymi płatnościami. 
Całość działa lokalnie na urządzeniu i wykorzystuje architekturę MVVM, co pozwala oddzielić interfejs od logiki i bazy danych.

## Kluczowe Funkcje Aplikacji
- Dodawanie wydatków i przychodów
- Śledzenie stanu konta
- Podział wydatków na kategorie
- Wizualizacja wydatków na wykresach
- Wizualizacja wydatków w czasie
- Planowanie przeszłych i cyklicznych wydatków
- Podział na konta użytkowników

## Użyte technologie

- **Baza danych**: Room Database
- **Język**: Kotlin
- **Architektura**: MVVM
- **UI**: Figma

## Mapa Ekranów
<img width="800" height="1480" alt="image" src="https://github.com/user-attachments/assets/a05fd837-4e8f-4374-82b0-217a3bff691f" />


## Makieta Aplikacji
Link do makiety aplikacji:

[Expense Tracker Mobile App](https://www.figma.com/make/2hZU3h7LnoRbklkYWf9zop/Expense-Tracking-Mobile-App?fullscreen=1&t=aYPW47rHdBPLfrtS-1&code-node-id=0-9&utm_source=chatgpt.com)




## Widok Bazy Danych

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/6dffc2c5-7e6e-4e8f-bed8-8dcff3feef83" />


## Architektura ViewModel
  Aplikacja została zbudowana zgodnie z zasadami Clean Architecture przy użyciu komponentów
  architekturalnych Android Jetpack.

   * Separacja logiki: Modele ViewModel pełnią rolę jedynego źródła prawdy dla interfejsu (Compose).
     Odpowiadają za zarządzanie stanem UI (UiState) oraz przetwarzanie zdarzeń użytkownika, izolując logikę
     biznesową od warstwy prezentacji.
     
   * Reaktywność (Flow/StateFlow): Wykorzystanie biblioteki Room wraz z Flow pozwala na automatyczne
     odświeżanie interfejsu. Każda zmiana w bazie danych (np. dodanie transakcji czy opłacenie rachunku)
     jest natychmiastowo emitowana do UI bez potrzeby ręcznego przeładowywania ekranów.
     
   * Współbieżność (Coroutines): Operacje wejścia/wyjścia (baza danych, preferencje użytkownika) są
     wykonywane asynchronicznie za pomocą viewModelScope, co gwarantuje płynność działania interfejsu (brak
     blokowania wątku głównego).
     
   * Warstwa Repozytorium: ViewModel nie komunikuje się bezpośrednio z bazą Room. Korzysta z
     wyspecjalizowanych repozytoriów, które abstrahują źródła danych i zapewniają czysty interfejs dla
     warstwy biznesowej.


## Strumienie danych w ViewModel

### HomeViewModel
  Zarządza głównym widokiem finansów i statystykami:
   * uiState (StateFlow\<HomeUiState\>): Kompleksowy strumień stanu, zawierający:
       * allTransactions: Pełna lista transakcji użytkownika.
       * filteredTransactions: Transakcje przefiltrowane według wybranego okresu (dzień, tydzień, miesiąc,
         rok).
       * totalIncome / totalExpenses: Wyliczone sumy przychodów i wydatków dla filtra.
       * chartData: Dane przygotowane do wykresu kołowego kategorii wydatków.
       * balanceHistory: Historia salda w czasie, wykorzystywana do wykresu liniowego.

### ScheduledPaymentsViewModel
  Odpowiada za logikę płatności zaplanowanych:
   * uiState (StateFlow\<ScheduledPaymentsUiState\>): Zarządza listami płatności:
       * upcomingPayments: Lista nadchodzących rachunków i zobowiązań.
       * paidPayments: Archiwum zrealizowanych płatności.
       * globalNetBalance: Aktualne saldo całkowite użytkownika pobrane z transakcji.

### AuthViewModel
  Zarządza procesem uwierzytelniania i sesją:
   * uiState (StateFlow\<AuthUiState\>): Informuje o stanie logowania, błędach walidacji oraz sukcesie
     autoryzacji (PIN/Email).




## Metody w ViewModel

### HomeViewModel
   * refreshUserSession(): Inicjalizuje obserwowanie danych transakcji po zidentyfikowaniu zalogowanego
     użytkownika.
   * setFilterPeriod(period): Zmienia zakres czasowy wyświetlanych danych i automatycznie przelicza
     statystyki oraz wykresy.
   * addTransaction(amount, category, description, date, type): Tworzy nową transakcję przypisaną do
     aktualnego profilu użytkownika.
   * deleteTransaction(id): Usuwa wybrany wpis z historii finansowej.

### ScheduledPaymentsViewModel
   * addPayment(...): Rejestruje nową płatność zaplanowaną (np. cykliczny abonament).
   * markAsPaid(id): Kluczowa funkcja logiczna – oznacza płatność jako wykonaną i automatycznie tworzy
     powiązaną z nią transakcję w historii głównej, aktualizując saldo.
   * deletePayment(id): Usuwa zaplanowane przypomnienie.

### AuthViewModel
   * registerUser(email, pin, confirmPin): Waliduje dane i tworzy nowe konto użytkownika w lokalnej bazie.
   * verifyPin(email, pin): Sprawdza poprawność kodu dostępu i przyznaje dostęp do aplikacji.
   * logout(): Czyści sesję użytkownika i resetuje stan UI.



## Warstwa Repozytorium

 ### TransactionRepository
  Zarządza historią wszystkich operacji finansowych:
   * getAllTransactions(userId): Zwraca reaktywny strumień wszystkich wpisów dla danego użytkownika.
   * insertTransaction(transaction): Zapisuje nowy przychód lub wydatek.
   * deleteTransactionById(id): Usuwa transakcję z bazy.

### ScheduledPaymentRepository
  Obsługuje płatności oczekujące:
   * getAllPayments(userId): Pobiera listę zaplanowanych płatności.
   * updatePayment(payment): Pozwala na aktualizację statusu (np. zmiana na "opłacone").
   * getPaymentById(id): Pobiera szczegóły konkretnego zobowiązania.

 ### AuthRepository
  Zarządza profilami użytkowników i bezpieczeństwem:
   * registerUser(email, pin): Obsługuje proces rejestracji i sprawdza unikalność adresu email.
   * checkIfUserExists(email): Weryfikuje obecność użytkownika w bazie przy logowaniu.
   * getUserByEmail(email): Pobiera dane profilu wraz z zaszyfrowanym (lokalnie) kodem PIN.
