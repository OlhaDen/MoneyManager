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







