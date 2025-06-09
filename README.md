# Task Manager REST API

REST API do zarządzania zadaniami, użytkownikami i powiadomieniami z obsługą uwierzytelniania i autoryzacji.

---

## Spis treści

- [Opis projektu](#opis-projektu)  
- [Funkcjonalności](#funkcjonalności)  
- [Technologie](#technologie)  
- [Instalacja](#instalacja)  
- [Konfiguracja](#konfiguracja)  
- [Użycie](#użycie)  
- [Role i uprawnienia](#role-i-uprawnienia)  
- [Struktura bazy danych](#struktura-bazy-danych)  
- [Testy](#testy)  
- [Plany rozwoju](#plany-rozwoju)  
- [Kontakt](#kontakt)

---

## Opis projektu

Task Manager REST API to backendowa aplikacja umożliwiająca zarządzanie zadaniami oraz użytkownikami w systemie z rozbudowanym mechanizmem uwierzytelniania i autoryzacji.  
Projekt pozwala na tworzenie, przypisywanie i komentowanie zadań, zarządzanie rolami użytkowników oraz obsługę powiadomień o zmianach.

---

## Funkcjonalności

- Rejestracja i logowanie użytkowników  
- Role: ADMIN i USER z różnymi poziomami uprawnień  
- Zarządzanie zadaniami: tworzenie, przypisywanie, filtrowanie po tagach i atrybutach  
- Dodawanie komentarzy do zadań  
- Powiadomienia o zdarzeniach systemowych  
- Pełna kontrola nad danymi użytkownika i zadaniami  
- Bezpieczne uwierzytelnianie (JWT) i autoryzacja  
- Obsługa hierarchii zadań (zadania nadrzędne i podrzędne)

---

## Technologie

- Java 17+  
- Spring Boot  
- Spring Security (JWT)  
- Hibernate / JPA  
- MySQL / MariaDB (lub inna relacyjna baza danych)  
- Maven / Gradle  
- JUnit 5 (testy jednostkowe)  

---

## Instalacja

1. Sklonuj repozytorium:  
   ```bash
   git clone https://github.com/twoj-repo/task-manager-api.git
   cd task-manager-api
