import csv
import os
import random
from datetime import timedelta
from faker import Faker

fake = Faker()

Faker.seed(12345)
random.seed(12345)

os.makedirs('./fakeDataGen/datas', exist_ok=True)


def generate_users(num_users=100):
    users = []
    for i in range(1, num_users + 1):
        phone_number = '0' + ''.join([str(random.randint(0, 9)) for _ in range(9)])
        users.append([
            i,  # user_id
            fake.user_name(),
            fake.first_name(),
            fake.last_name(),
            fake.email(),
            phone_number,
            fake.address().replace('\n', ', '),
            fake.date_time_between(start_date='-3y', end_date='now').strftime('%Y-%m-%d %H:%M:%S')
        ])

    with open('./fakeDataGen/datas/users.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(
            ['user_id', 'username', 'firstname', 'lastname', 'email', 'phone', 'address', 'registration_date'])
        writer.writerows(users)


def generate_admins(num_admins=5):
    admins = []
    for i in range(1, num_admins + 1):
        admins.append([
            i,  # admin_id
            fake.user_name(),
            fake.email(),
            fake.password(length=12)
        ])

    with open('./fakeDataGen/datas/admins.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['admin_id', 'username', 'email', 'password'])
        writer.writerows(admins)


def generate_documents(num_docs=1000):
    documents = []


isbn = ''.join([str(fake.random_digit()) for _ in range(10)])
for i in range(1, num_docs + 1):
    documents.append([
        i,  # document_id
        fake.catch_phrase(),  # as title
        fake.name(),  # as author
        fake.text(max_nb_chars=1000),  # as description
        fake.company(),  # as publisher
        fake.date_between(start_date='-15y', end_date='today').strftime('%Y'),
        isbn,
        random.randint(1, 100)  # quantity
    ])

with open('./fakeDataGen/datas/documents.csv', 'w', newline='') as f:
    writer = csv.writer(f)
    writer.writerow(['document_id', 'title', 'author', 'description', 'publisher', 'publish_year','isbn', 'quantity'])
    writer.writerows(documents)


def generate_borrows(num_borrows=80):
    borrows = []
    for i in range(1, num_borrows + 1):
        borrow_date = fake.date_time_between(start_date='-1y', end_date='now')
        due_date = borrow_date + timedelta(days=14)
        return_date = None

        # 70% chance the book has been returned
        if random.random() < 0.7:
            return_date = borrow_date + timedelta(days=random.randint(1, 20))

        fine_amount = 0
        if return_date and return_date > due_date:
            days_late = (return_date - due_date).days
            fine_amount = days_late * 0.5  # $0.50 per day

        borrows.append([
            i,  # borrow_id
            random.randint(1, num_users),  # user_id
            random.randint(1, num_docs),  # document_id
            borrow_date.strftime('%Y-%m-%d %H:%M:%S'),
            due_date.strftime('%Y-%m-%d %H:%M:%S'),
            return_date.strftime('%Y-%m-%d %H:%M:%S') if return_date else None,
        ])

    with open('./fakeDataGen/datas/borrow.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['borrow_id', 'user_id', 'document_id', 'borrow_date', 'due_date', 'return_date'])
        writer.writerows(borrows)


if __name__ == "__main__":
    num_users = 100
    num_docs = 500
    generate_users(num_users)
    generate_admins()
    generate_documents(num_docs)
    generate_borrows()
    print("CSV files generated successfully!")
