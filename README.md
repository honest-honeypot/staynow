# Honest Honeypot — П4 «СтейНау»

Репозиторій команди з дисципліни «Технології забезпечення якості програмних засобів»,
командний формат лабораторних робіт.

**Продукт:** П4 «СтейНау» — система онлайн-бронювання для малих готелів і гостьових будинків.
**Demo-застосунок поточної версії:** [automationintesting.online](https://automationintesting.online)
**Поточний спринт:** 3 — «Тест-кейси і дефекти».

## Склад команди

| ПІБ | GitHub | Роль у спринті 1 |
|---|---|---|
| Оніщук Максим | [Makhimn](https://github.com/Makhimn) | Керівник тестування |
| Оліневич Богдан | [alkawforever88](https://github.com/alkawforever88) | Тест-аналітик |
| Вориняк Василь | [honest-honeypot](https://github.com/honest-honeypot) | Інженер з тестування |
| Валюх Артем | [ArtemValiukh](https://github.com/ArtemValiukh) | Скрам-майстер / рецензент |
| Журавський Владислав | [vladzh20](https://github.com/vladzh20) | Аудитор-опонент |

Ролі змінюються щоспринта за графіком ротації у [статуті команди](docs/team/charter.md).

## Артефакти спринта 1

| Артефакт | Файл | Відповідальний |
|---|---|---|
| Характеристика продукту, ролі, процеси, таблиці елементів BPMN | [docs/sprint1/product.md](docs/sprint1/product.md) | Керівник тестування, Тест-аналітик |
| Беклог user stories і трасування «Очікування → Stories» | [docs/sprint1/backlog.md](docs/sprint1/backlog.md) | Тест-аналітик |
| BPMN-моделі бізнес-процесів | [docs/sprint1/bpmn/](docs/sprint1/bpmn/) | Інженер з тестування |
| Протокол рецензування моделей | [docs/sprint1/bpmn/review.md](docs/sprint1/bpmn/review.md) | Скрам-майстер / рецензент |
| Таблиця розподілу задач | [docs/sprint1/tasks.md](docs/sprint1/tasks.md) | Скрам-майстер |
| Акт аудиту | [docs/sprint1/audit.md](docs/sprint1/audit.md) | Аудитор-опонент |
| Протокол ретроспективи | [docs/sprint1/retro.md](docs/sprint1/retro.md) | Скрам-майстер |
| Статут команди | [docs/team/charter.md](docs/team/charter.md) | уся команда |

## Артефакти спринта 2

| Артефакт | Файл | Відповідальний |
|---|---|---|
| Реєстр дефектів вимог | [docs/sprint2/requirements-defects.xlsx](docs/sprint2/requirements-defects.xlsx) | Тест-аналітик |
| Виправлена специфікація SRS 1.1 з журналом змін | [docs/sprint2/SRS-1.1.md](docs/sprint2/SRS-1.1.md) | Тест-аналітик |
| План тестування: рівні, типи, інструменти | [docs/sprint2/test-plan.md](docs/sprint2/test-plan.md) | Інженер з тестування |
| Таблиця задач і розподіл розділів SRS | [docs/sprint2/task-board.md](docs/sprint2/task-board.md) | Скрам-майстер, Керівник тестування |
| Протокол ретроспективи | [docs/sprint2/retro-sprint2.md](docs/sprint2/retro-sprint2.md) | Скрам-майстер |

## Артефакти спринта 3

| Артефакт | Файл | Відповідальний |
|---|---|---|
| Тест-кейси, покриття вимог, протокол рецензування | [docs/sprint3/test-cases.xlsx](docs/sprint3/test-cases.xlsx) | Тест-аналітик |
| Журнал виконання і середовище | [docs/sprint3/execution-log.xlsx](docs/sprint3/execution-log.xlsx) | Інженер з тестування |
| Звіти про дефекти і вкладення | [docs/sprint3/bug-reports.md](docs/sprint3/bug-reports.md) | Інженер з тестування |
| Акт аудитора-опонента | [docs/sprint3/audit.md](docs/sprint3/audit.md) | Аудитор-опонент |
| Таблиця розподілу задач | [docs/sprint3/task-board.md](docs/sprint3/task-board.md) | Скрам-майстер |
| Протокол ретроспективи | [docs/sprint3/retro.md](docs/sprint3/retro.md) | Скрам-майстер |
| Зведення спринта: статистика і покриття | [docs/sprint3/README.md](docs/sprint3/README.md) | Керівник тестування |

**Трекер дефектів:** [GitHub Issues цього репозиторію, мітка `sprint:3`](https://github.com/honest-honeypot/staynow/issues?q=is%3Aissue+label%3A%22sprint%3A3%22).

**Підсумок прогону Run 1:** 46 тест-кейсів — 30 passed, 11 failed, 5 blocked;
11 звітів про дефекти п'яти рівнів severity.

## BPMN-моделі

| № | Процес | Джерело | Експорт |
|---|---|---|---|
| 01 | Бронювання номера | [01_booking.drawio](docs/sprint1/bpmn/01_booking.drawio) | [01_booking.pdf](docs/sprint1/bpmn/01_booking.pdf) |
| 02 | Заселення і виселення | [02_checkin_checkout.drawio](docs/sprint1/bpmn/02_checkin_checkout.drawio) | [02_checkin_checkout.pdf](docs/sprint1/bpmn/02_checkin_checkout.pdf) |
| 03 | Скасування і повернення коштів | [03_cancellation_refund.drawio](docs/sprint1/bpmn/03_cancellation_refund.drawio) | [03_cancellation_refund.pdf](docs/sprint1/bpmn/03_cancellation_refund.pdf) |
| 04 | Обробка повідомлень гостей | [04_guest_messages.drawio](docs/sprint1/bpmn/04_guest_messages.drawio) | [04_guest_messages.pdf](docs/sprint1/bpmn/04_guest_messages.pdf) |

Моделі побудовано в **diagrams.net** з бібліотекою фігур BPMN 2.0 за текстовими
таблицями елементів із `product.md`. Щоб відкрити джерело:
[app.diagrams.net](https://app.diagrams.net) → *File → Open From → Device*.
Деталі — у [docs/sprint1/bpmn/README.md](docs/sprint1/bpmn/README.md).

## Структура репозиторію

```
README.md                      цей файл
docs/
  team/
    charter.md                 склад команди, ротація ролей, домовленості, методологія
  sprint1/
    product.md                 профіль продукту, ролі, процеси, таблиці елементів BPMN
    backlog.md                 user stories і трасування до очікувань замовника
    tasks.md                   розподіл задач спринта
    audit.md                   акт аудитора-опонента
    retro.md                   ретроспектива Start / Stop / Continue
    bpmn/
      README.md                інструмент, склад моделей, елементи, правила експорту
      review.md                протокол рецензування за чек-листом із 10 пунктів
      NN_*.drawio              вихідні файли моделей
      NN_*.pdf                 експорт для перегляду без редактора
  sprint2/
    README.md                  перелік артефактів спринта
    requirements-defects.xlsx  реєстр дефектів вимог: аркуші «Реєстр», «Спірні», «Розподіл розділів»
    SRS-1.1.md                 виправлена специфікація з журналом змін
    test-plan.md               рівні й типи тестування, інструменти, обмеження
    task-board.md              задачі спринта і розподіл розділів SRS між учасниками
    retro-sprint2.md           ретроспектива Start / Stop / Continue
  sprint3/
    README.md                  зведення спринта: статистика, покриття, середовище
    test-cases.xlsx            кейси, аркуші «Test cases», «Coverage», «Review»
    execution-log.xlsx         журнал прогону, аркуші «Run 1», «Environment», «Summary»
    bug-reports.md             звіти про дефекти, продубльовані в GitHub Issues
    attachments/               знімки екрана до звітів, іменування за Bug ID
    audit.md                   акт аудитора-опонента: відтворення дефектів і DoD
    task-board.md              задачі спринта і розподіл вимог між учасниками
    retro.md                   ретроспектива Start / Stop / Continue
```

Артефакти наступних спринтів додаються теками `docs/sprint2/` … `docs/sprint5/`;
структура тек не змінюється.
