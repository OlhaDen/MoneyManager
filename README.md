# MoneyManager
Aplikacja służy do zarządzania finansami osobistymi i działa jako prosty system do zapisywania przychodów oraz wydatków. 
Użytkownik najpierw zakłada konto lub się loguje za pomocą adresu e-mail i PIN-u. Po zalogowaniu trafia na ekran główny, gdzie widzi swoje saldo, przychody, wydatki oraz ostatnie transakcje. 
Może dodawać nowe operacje finansowe, które są zapisywane w lokalnej bazie danych (SQLite przez Room). 
Każda transakcja jest przypisana do konkretnego użytkownika przez `userId`, dzięki czemu dane różnych użytkowników są od siebie oddzielone. 
Aplikacja automatycznie aktualizuje saldo i listę transakcji po każdej zmianie. Dodatkowo użytkownik może przeglądać historię operacji oraz zarządzać zaplanowanymi płatnościami. 
Całość działa lokalnie na urządzeniu i wykorzystuje architekturę MVVM, co pozwala oddzielić interfejs od logiki i bazy danych.
