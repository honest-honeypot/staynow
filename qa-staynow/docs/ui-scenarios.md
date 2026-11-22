# UI-сценарії для автоматизації Selenium WebDriver

**Автор:** Вориняк Василь (Тест-аналітик)  
**Джерело:** `docs/sprint3/test-cases.xlsx`, `docs/sprint2/SRS-1.1.md`  
**Demo-застосунок:** `https://automationintesting.online`

| ID кейсу (S3) | Вимога SRS | Сценарій | Кроки та локатори | Явне очікування (`WebDriverWait`) | Тест-клас і метод |
|---|---|---|---|---|---|
| TC-001 | F-10 | Навігація «Rooms» відкриває секцію номерів | Відкрити `/`, клікнути `a.nav-link[href*='#rooms']`, перевірити видимість секції номерів | `visibilityOfElementLocated(By.cssSelector("a.nav-link[href*='#rooms']"))` і заголовка `Our Rooms` | `HomeAndContactUiTest#roomsNavigationShowsOurRoomsSection` |
| TC-002 | F-10 | Посилання «Admin» веде на сторінку входу | Відкрити `/`, клікнути `a.nav-link[href='/admin']`, перевірити URL `/admin` та форму входу | `urlContains("/admin")` та `visibilityOfElementLocated(By.id("username"))` | `HomeAndContactUiTest#adminLinkOpensLoginPage` |
| TC-003 | F-20 | Картки номерів у «Our Rooms» містять типи та «Book now» | Відкрити `/`, знайти картки `h5.card-title` (`Single`, `Double`, `Suite`) та посилання `a[href*='/reservation/']` | `numberOfElementsToBeMoreThan(By.cssSelector("a[href*='/reservation/']"), 0)` | `HomeAndContactUiTest#roomCardsDisplayTypesAndBookNowLinks` |
| TC-004 | F-20 | Клік «Book now» переносить дати `checkin`/`checkout` у URL номера | Відкрити `/`, клікнути перше посилання `a[href*='/reservation/']`, перевірити URL | `urlContains("/reservation/")` | `HomeAndContactUiTest#bookNowTransfersDatesToReservationUrl` |
| TC-042 | GUI-85 | Контактна форма містить 5 полів і кнопку «Submit» | Відкрити `/`, перевірити наявність `#name`, `#email`, `#phone`, `#subject`, `#description` і кнопки `Submit` | `visibilityOfElementLocated(By.id("name"))` | `HomeAndContactUiTest#contactFormContainsAllRequiredFieldsAndSubmit` |
| TC-017 | F-70 | Контактна форма відхиляє тему з 4 символів (`ABCD`) | Заповнити `#name`, `#email`, `#phone`, `#subject`=`ABCD`, `#description`=`M`×20, натиснути `Submit` | `visibilityOfElementLocated(By.cssSelector(".alert.alert-danger"))` | `HomeAndContactUiTest#contactFormRejectsFourCharSubject` |
| TC-015 | F-70, GUI-85 | Контактна форма приймає мінімальні валідні дані (5/20 символів) | Заповнити `#name`=`Test Guest`, `#email`, `#phone`, `#subject`=`ABCDE`, `#description`=`M`×20, натиснути `Submit` | `invisibilityOfElementLocated(By.id("subject"))` та поява тексту подяки | `HomeAndContactUiTest#contactFormAcceptsMinimalValidInputAndShowsThanks` |
| TC-038 | GUI-70 | Сторінка входу адміністратора має поля, placeholder-и та «Front Page» | Відкрити `/admin`, перевірити заголовок `Login`, `#username`, `#password` (`type=password`), `#doLogin`, `#frontPageLink` | `visibilityOfElementLocated(By.id("username"))` | `AdminAuthUiTest#adminLoginPageDisplaysExpectedFieldsAndAttributes` |
| TC-024 | F-80 | Хибний пароль відхиляє вхід адміністратора | На `/admin` ввести `admin` / `wrong-password-1`, клікнути `#doLogin`, перевірити `.alert.alert-danger` | `visibilityOfElementLocated(By.cssSelector(".alert.alert-danger"))` | `AdminAuthUiTest#invalidPasswordShowsErrorAndBlocksAccess` |
| TC-023 | F-80 | Успішний вхід адміністратора (`admin`/`password`) | На `/admin` ввести `admin` / `password`, клікнути `#doLogin`, перевірити вхід в адмінпанель | `invisibilityOfElementLocated(By.id("doLogin"))` | `AdminAuthUiTest#validAdminLoginOpensAdminPanel` |
| TC-025 | F-85 | «Logout» завершує сеанс адміністратора | Увійти як `admin`/`password`, натиснути `Logout`, знову відкрити `/admin` і перевірити появу форми входу | `invisibilityOfElementLocated(By.id("doLogin"))` → клік `Logout` → `visibilityOfElementLocated(By.id("doLogin"))` | `AdminAuthUiTest#logoutEndsAdminSession` |
