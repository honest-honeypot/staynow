# Протокол Code Review тест-класів — Sprint 4

**Команда:** Honest Honeypot · **Продукт:** П4 «СтейНау» (`qa-staynow`)
**Рецензент:** Оліневич Богдан (`@alkawforever88`, Скрам-майстер / рецензент)
**Дата перевірки:** 23.11.2026
**База перевірки:** Чек-лист code review тесту (8 пунктів), `docs/sprint2/SRS-1.1.md`.

---

## Зведена таблиця статусів

| Тест-клас | Автор | Тест-методи | Вимоги | Статус рецензії |
|---|---|---|---|---|
| `BookingValidatorTest.java` | Вориняк Василь (`@honest-honeypot`) | 7 | BL-10, BL-20 | **Approved** (виправлено за зауваженнями) |
| `AvailabilityTest.java` (BL-60) | Оніщук Максим (`@Makhimn`) | 7 | BL-60 | **Approved** (виправлено за зауваженнями) |
| `AvailabilityTest.java` (BL-70) | Оліневич Богдан (`@alkawforever88`) | 3 | BL-70 | Передано на перехресне рев'ю колезі |
| `PriceCalculatorTest.java` | Журавський Владислав (`@vladzh20`) | 3 | BL-30, BL-40, BL-50 | **Approved** (виправлено за зауваженнями) |
| Клас граничних значень | Валюх Артем (`@ArtemValiukh`) | 0 | BL-10, BL-50, BL-60 | Очікується коміт |
| UI WebDriver тести (`*UiTest.java`) | Оніщук Максим (`@Makhimn`) | 0 | F-10..F-85, GUI-70..85 | Очікується коміт |

---

## 1. `BookingValidatorTest.java` — `@honest-honeypot`

**Перевірено методи (7):** `validBookingReturnsNoErrors`, `checkOutNotAfterCheckInReturnsError`, `nightsBoundariesValidation`, `checkInInPastWithFutureCheckOutReturnsError`, `guestsBoundariesValidation`, `emailAndPhoneFormatsValidation`, `fullValidateChecksContactsAndRejectsNullDates`.

| № | Пункт чек-листа | Так / Ні | Зауваження |
|---|---|---|---|
| 1 | Правило з BL-вимоги, ID у `@DisplayName`, очікування з SRS (не з коду) | Так | Є `@Tag("unit")` та `@DisplayName` / `name` з ID `BL-10`, `BL-20`. Очікування відповідають SRS v1.1, а не багам `v1`. |
| 2 | Структура AAA читається; один тест — одна причина впасти | Частково | AAA чітко розмічено. Метод `fullValidateChecksContactsAndRejectsNullDates` поєднує дві незалежні перевірки (невалідні контакти `BL-20` і `NullPointerException` при `null`-датах `BL-10`) — бажано винести `null` в окремий тест. |
| 3 | Ім'я методу описує поведінку, а не номер | Так | Усі 7 імен описують умову й очікуваний результат. |
| 4 | Граничні значення з обох боків межі; `@ParameterizedTest` замість копій | Так | Використано `@ParameterizedTest` + `@CsvSource` для ночей (`1, 2, 29, 30, 31`), гостей (`0, 1, 2, 3`) і порядку дат. |
| 5 | Винятки через `assertThrows`; `BigDecimal` зі шкалою | Так | Використано `assertThrows(NullPointerException.class, ...)` та `assertAll`. (`BigDecimal` у класі не застосовується.) |
| 6 | Незалежність тестів, `@BeforeEach` | Так | `BookingValidator` створюється у `@BeforeEach`, константа `TODAY` незмінна. |
| 7 | Якість UI-тесту (без `Thread.sleep`, явні очікування, стійкі локатори) | Н/З | Юніт-тест. |
| 8 | Падаючі тести мають ID дефекту в коментарі; немає «підганянь» і `@Disabled` | Ні → **Так** | Обидва падаючі тести (`nightsBoundariesValidation` для 30 ночей та `checkInInPastWithFutureCheckOutReturnsError`) потребували посилань на Issues `#17` та `#18`. |

**Обов'язкові правки:**
1. Додати коментар `// BUG-03 (Issue #17)` над `nightsBoundariesValidation`.
2. Додати коментар `// BUG-04 (Issue #18)` над `checkInInPastWithFutureCheckOutReturnsError`.

**Рекомендації (неблокуючі):**
1. Замінити тестовий email `guest.test+1@staynow.ua` у `emailAndPhoneFormatsValidation` на домен `example.com`.
2. За можливості розбити `fullValidateChecksContactsAndRejectsNullDates` на два окремих тести (контакти `BL-20` і `null`-дати `BL-10`).

> **UPD:** Зауваження виправлено, посилання на Issues `#17` і `#18` додано. Статус рев'ю: **Approved** (Оліневич Богдан, 23.11.2026).

---

## 2. `AvailabilityTest.java` (частина `BL-60`) — `@Makhimn`

**Перевірено методи (7):** `cancellationFeeFreeAtSevenOrMoreDays`, `cancellationFeeHalfBetweenThreeAndSixDays`, `cancellationFeeFullLessThanThreeDays`, `cancellationFeeAndRefundRoundedHalfUp`, `refundMatchesTotalMinusFeeAcrossTiers`, `zeroTotalReturnsZeroFeeAndRefund`, `negativeOrNullTotalThrowsIllegalArgumentException`.

| № | Пункт чек-листа | Так / Ні | Зауваження |
|---|---|---|---|
| 1 | Правило з BL-вимоги, ID у `@DisplayName`, очікування з SRS (не з коду) | Так | Є `@Tag("unit")`, кожен метод має `@DisplayName` або `name` із `BL-60`; очікування за SRS v1.1. |
| 2 | Структура AAA читається; один тест — одна причина впасти | Так | Усі методи структуровані за Arrange–Act–Assert, перевіряють конкретне правило. |
| 3 | Ім'я методу описує поведінку, а не номер | Так | Назви зрозумілі й самодокументовані. |
| 4 | Граничні значення з обох боків межі; `@ParameterizedTest` замість копій | Так | Покрито пороги днів (`0, 1, 2`, `3, 4, 6`, `7, 8, 30`) через `@ValueSource` і `@CsvSource`, перевірено `total = 0.00` та округлення `HALF_UP` (`50.005 -> 50.01`, `125.025 -> 125.03`). |
| 5 | Винятки через `assertThrows`; `BigDecimal` зі шкалою | Так | Винятки перевірено через `assertThrows(IllegalArgumentException.class, ...)` для `-0.01`, `null` та `refund(-10.00)`; усі `BigDecimal` зі шкалою 2 (`"0.00"`, `"500.00"`). |
| 6 | Незалежність тестів, `@BeforeEach` | Так | `availability = new Availability()` створюється у `@BeforeEach`. |
| 7 | Якість UI-тесту (без `Thread.sleep`, явні очікування, стійкі локатори) | Н/З | Юніт-тест. |
| 8 | Падаючі тести мають ID дефекту в коментарі; немає «підганянь» і `@Disabled` | Ні → **Так** | Над падаючим тестом `cancellationFeeFreeAtSevenOrMoreDays` була заглушка `// BUG-MODULE-03 (Issue #__)` замість `// BUG-05 (Issue #19)`. |

**Обов'язкові правки:**
1. Замінити коментар над `cancellationFeeFreeAtSevenOrMoreDays` на `// BUG-05 (Issue #19)`.

> **UPD:** Зауваження виправлено, посилання `// BUG-05 (Issue #19)` підставлено. Статус рев'ю: **Approved** (Оліневич Богдан, 23.11.2026).

> **Примітка щодо частини `BL-70` (`@alkawforever88`, 3 методи):** перевірку 3 методів `BL-70` передано на перехресне рев'ю колезі (`@vladzh20` або `@honest-honeypot`). Коментар `// BUG-02 (Issue #16)` над падаючим тестом уже додано; за бажанням можна дописати тест на `assertThrows(IllegalStateException.class, () -> availability.book(...))`.

---

## 3. `PriceCalculatorTest.java` — `@vladzh20`

**Перевірено методи (3):** `highSeasonShouldHaveFactor130`, `fridayShouldBeWeekendNight`, `sevenNightsShouldGetDiscount`.

| № | Пункт чек-листа | Так / Ні | Зауваження |
|---|---|---|---|
| 1 | Правило з BL-вимоги, ID у `@DisplayName`, очікування з SRS (не з коду) | Ні → **Так** | Були відсутні `@Tag("unit")` і `@DisplayName("BL-XX: ...")` (ID вимог були лише в коментарях `//`). |
| 2 | Структура AAA читається; один тест — одна причина впасти | Так | Кроки підготовки, виклику та перевірки зрозумілі, кожен метод перевіряє одне правило. |
| 3 | Ім'я методу описує поведінку, а не номер | Так | Назви чіткі й описують поведінку. |
| 4 | Граничні значення з обох боків межі; `@ParameterizedTest` замість копій | Ні → **Так** | Потребувалося додати граничні переходи сезонів (`31.05`/`01.06`, `31.08`/`01.09`, `19.12`/`20.12`, `10.01`/`11.01`), низький (`0.80`) і звичайний (`1.00`) сезони, `@ParameterizedTest` та тест на округлення `HALF_UP` у `nightPrice()`. |
| 5 | Винятки через `assertThrows`; `BigDecimal` зі шкалою | Ні → **Так** | Потребувалися тести `assertThrows(IllegalArgumentException.class, ...)` на недодатний тариф і `checkOut <= checkIn` (`BL-50`) та заміна `compareTo` на `assertEquals(new BigDecimal("..."), actual)` зі шкалою 2. |
| 6 | Незалежність тестів, `@BeforeEach` | Частково → **Так** | Перенести ініціалізацію `PriceCalculator` у `@BeforeEach`. |
| 7 | Якість UI-тесту (без `Thread.sleep`, явні очікування, стійкі локатори) | Н/З | Юніт-тест. |
| 8 | Падаючі тести мають ID дефекту в коментарі; немає «підганянь» і `@Disabled` | Ні → **Так** | Над падаючим тестом `fridayShouldBeWeekendNight` був відсутній коментар `// BUG-01 (Issue #15)`. |

**Обов'язкові правки:**
1. Додати `@Tag("unit")` на клас і `@DisplayName("BL-XX: ...")` на клас та всі методи.
2. Додати коментар `// BUG-01 (Issue #15)` над `fridayShouldBeWeekendNight`.
3. Перенести створення `PriceCalculator` у `@BeforeEach` і замінити `compareTo` на `assertEquals(new BigDecimal("..."), actual)` зі шкалою 2.
4. Додати (самостійно або узгодити з `@ArtemValiukh`) граничні перевірки сезонів, порога 6/7 ночей, `assertThrows(IllegalArgumentException.class, ...)` та перевірку округлення `HALF_UP` у `nightPrice()`.

> **UPD:** Усі обов'язкові зауваження виправлено. Статус рев'ю: **Approved** (Оліневич Богдан, 23.11.2026).

---

---

## 4. `HomeAndContactUiTest.java` — `@Makhimn`

**Перевірено методи (7):** `roomsNavigationShowsOurRoomsSection`, `adminLinkOpensLoginPage`, `roomCardsDisplayTypesAndBookNowLinks`, `bookNowTransfersDatesToReservationUrl`, `contactFormContainsAllRequiredFieldsAndSubmit`, `contactFormRejectsFourCharSubject`, `contactFormAcceptsMinimalValidInputAndShowsThanks`.

| № | Пункт чек-листа | Так / Ні | Зауваження |
|---|---|---|---|
| 1 | Правило з вимоги, ID у `@DisplayName`, очікування з SRS / кейсів S3 | Так | Є `@Tag("ui")`, у кожному `@DisplayName` вказано `TC-XXX` та вимогу SRS (`F-10`, `F-20`, `F-70`, `GUI-85`); усі 7 методів відповідають `ui-scenarios.md`. |
| 2 | Структура AAA читається; один тест — одна причина впасти | Так | Кожен тест перевіряє окремий сценарій із чіткою послідовністю дій та перевірок через `assertAll`. |
| 3 | Ім'я методу описує поведінку, а не номер | Так | Назви чіткі й описують сценарій і очікуваний результат (`contactFormRejectsFourCharSubject` тощо). |
| 4 | Граничні значення з обох боків межі | Так | Перевірено межу довжини теми контактної форми: 4 символи (`ABCD` — відхилення) проти 5 символів (`ABCDE`) та 20 символів опису (успіх). |
| 5 | Коректні перевірки й повідомлення помилок | Так | Використано `assertAll` із діагностичними повідомленнями фактичного тексту / URL. |
| 6 | Незалежність тестів, `@BeforeEach` / `@AfterEach` | Так | Окрема сесія `WebDriver` створюється у `@BeforeEach` через `WebDriverFactory.create()` і гарантовано закривається `driver.quit()` у `@AfterEach`. |
| 7 | Якість UI-тесту (без `Thread.sleep`, явні очікування, стійкі локатори) | Так | `Thread.sleep` відсутній; використано `WebDriverWait` (15 с) та `ExpectedConditions`; локатори стабільні (`By.id`, `By.cssSelector`, `normalize-space()`); тестовий email на домені `example.com`. |
| 8 | Немає «підганянь» і `@Disabled` без причини | Так | `@Disabled` відсутні, перевіряється реальна поведінка застосунку. |

**Неблокуюча примітка:** хелпер `scrollAndClick` виконує клік через `JavascriptExecutor` (`arguments[0].click()`) для обходу перекриттів у headless-режимі — це робоче рішення, зауважень немає.

> **Статус рев'ю:** **Approved** (Оліневич Богдан, 23.11.2026).

---

## 5. `AdminAuthUiTest.java` та `WebDriverFactory.java` — `@Makhimn`

**Перевірено методи (4):** `adminLoginPageDisplaysExpectedFieldsAndAttributes`, `invalidPasswordShowsErrorAndBlocksAccess`, `validAdminLoginOpensAdminPanel`, `logoutEndsAdminSession` + допоміжний клас `WebDriverFactory`.

| № | Пункт чек-листа | Так / Ні | Зауваження |
|---|---|---|---|
| 1 | Правило з вимоги, ID у `@DisplayName`, очікування з SRS / кейсів S3 | Так | Є `@Tag("ui")`, у `@DisplayName` вказано `TC-023..025`, `TC-038` та вимоги `F-80`, `F-85`, `GUI-70`; повна відповідність `ui-scenarios.md`. |
| 2 | Структура AAA читається; один тест — одна причина впасти | Так | Повторюване введення облікових даних винесено у приватний метод `submitCredentials`, кожен тест перевіряє один сценарій. |
| 3 | Ім'я методу описує поведінку, а не номер | Так | Усі 4 імена описують перевірювану поведінку (`invalidPasswordShowsErrorAndBlocksAccess`, `logoutEndsAdminSession` тощо). |
| 4 | Позитивні й негативні сценарії | Так | Перевірено форму й атрибути полів (`type="password"`, `placeholder`), відмову з хибним паролем, успішний вхід і завершення сеансу після `Logout`. |
| 5 | Коректні перевірки й повідомлення помилок | Так | Використано `assertAll` із перевіркою тексту помилки, видимості форми, URL та відсутності доступу після `Logout`. |
| 6 | Незалежність тестів, `@BeforeEach` / `@AfterEach` | Так | Кожен тест отримує чистий екземпляр браузера у `@BeforeEach` і закриває його у `@AfterEach`. |
| 7 | Якість UI-тесту (без `Thread.sleep`, явні очікування, стійкі локатори) | Так | Без `Thread.sleep`; явні `WebDriverWait` (включно з лямбда-умовою після `Logout`); стійкі локатори за `id` (`#username`, `#password`, `#doLogin`, `#frontPageLink`) і CSS. У `WebDriverFactory` реалізовано headless за замовчуванням із перемикачем `-Dheadless=false` і підтримкою Chrome/Firefox (`1280×900`). |
| 8 | Немає «підганянь» і `@Disabled` без причини | Так | `@Disabled` і фіктивних перевірок немає. |

> **Статус рев'ю:** **Approved** (Оліневич Богдан, 23.11.2026).
