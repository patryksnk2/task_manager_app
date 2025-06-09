# Task Manager REST API

## Opis projektu

Task Manager REST API to zaawansowany backend do zarządzania zadaniami, użytkownikami oraz powiadomieniami w systemie.  
System umożliwia rejestrację, logowanie, przypisywanie zadań, komentowanie, filtrowanie zadań po tagach i atrybutach oraz obsługę powiadomień.  
Projekt uwzględnia mechanizmy bezpieczeństwa: uwierzytelnianie i autoryzację za pomocą JWT oraz role użytkowników (ADMIN, USER).

---

## Spis treści

- [Opis projektu](#opis-projektu)  
- [Funkcjonalności](#funkcjonalności)  
- [Architektura](#architektura)  
- [Technologie](#technologie)  
- [Struktura bazy danych](#struktura-bazy-danych)  
- [Role i uprawnienia](#role-i-uprawnienia)  
- [Instalacja i uruchomienie](#instalacja-i-uruchomienie)  
- [Endpoints API](#endpoints-api)  
- [Testy](#testy)  
- [Plany rozwoju](#plany-rozwoju)  
- [Wyzwania i rozwiązania](#wyzwania-i-rozwiazania)  
- [Kontakt](#kontakt)

---

## Funkcjonalności

- **Rejestracja i logowanie** użytkowników z walidacją danych i szyfrowaniem haseł  
- **Role RBAC:** ADMIN i USER z różnymi poziomami uprawnień  
- **Zarządzanie zadaniami:** tworzenie, edycja, przypisywanie użytkowników, hierarchia zadań (zadania nadrzędne i podrzędne)  
- **Komentarze do zadań** z możliwością wątku odpowiedzi  
- **Tagi i atrybuty zadań:** definiowanie, przypisywanie, filtrowanie  
- **Powiadomienia:** wewnętrzne (na koncie użytkownika) i planowane rozszerzenie o powiadomienia e-mail  
- **Bezpieczeństwo:** JWT, szyfrowanie, autoryzacja na poziomie endpointów  
- **Stateless API:** brak sesji, skalowalność

---

## Architektura

Projekt oparty jest na warstwowej architekturze:  
- **Warstwa prezentacji:** REST API (Spring MVC)  
- **Warstwa serwisowa:** logika biznesowa  
- **Warstwa dostępu do danych:** JPA/Hibernate  
- **Warstwa bezpieczeństwa:** Spring Security z JWT  
- **Baza danych:** relacyjna (MySQL / MariaDB lub inna)

---

## Technologie

- Java 17+  
- Spring Boot 3.x  
- Spring Security (JWT)  
- Hibernate / JPA  
- Maven / Gradle  
- MySQL / MariaDB (lub inna relacyjna baza danych)  
- JUnit 5, Mockito (testy jednostkowe)  

---

## Struktura bazy danych

Tabela i relacje główne:  

| Tabela              | Opis                                              |
|---------------------|---------------------------------------------------|
| `users`             | Użytkownicy systemu                                |
| `roles`             | Role użytkowników (np. ADMIN, USER)                |
| `user_roles`        | Przypisanie ról do użytkowników                    |
| `tasks`             | Zadania, w tym tytuł, opis, status, priorytet     |
| `task_attributes`   | Atrybuty zadań (status, priorytet itd.)            |
| `tags`              | Tagowanie zadań                                    |
| `task_tags`         | Przypisanie tagów do zadań                         |
| `task_assigned_users`| Przypisanie użytkowników do zadań                  |
| `task_comments`     | Komentarze do zadań, wątki komentarzy              |
| `notifications`     | Powiadomienia użytkowników                          |

Relacje zapewniają integralność danych i ułatwiają rozbudowę.

---

## Role i uprawnienia

| Rola  | Uprawnienia                                                                                  |
|-------|---------------------------------------------------------------------------------------------|
| ADMIN | Pełny dostęp: zarządzanie użytkownikami, zadaniami, tagami, powiadomieniami oraz konfiguracją |
| USER  | Dostęp do własnych zadań: przeglądanie, komentowanie, tworzenie zadań, filtrowanie po tagach |

---

## Instalacja i uruchomienie

1. **Klonuj repozytorium:**  
   ```bash
   git clone https://github.com/twoj-repo/task-manager-api.git
   cd task-manager-api
