# Honest Honeypot — П4 «СтейНау»

Репозиторій команди з дисципліни «Технології забезпечення якості програмних засобів»,
командний формат лабораторних робіт.

**Продукт:** П4 «СтейНау» — система онлайн-бронювання для малих готелів і гостьових будинків.
**Demo-застосунок поточної версії:** [automationintesting.online](https://automationintesting.online)
**Поточний спринт:** 1 — «Продукт і вимоги».

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
```

Артефакти наступних спринтів додаються теками `docs/sprint2/` … `docs/sprint5/`;
структура тек не змінюється.
