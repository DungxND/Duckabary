import csv
from faker import Faker
from datetime import datetime, timedelta
import random
import os

fake = Faker()

# Set random seed for reproducibility
Faker.seed(12345)
random.seed(12345)

# Ensure the ./datas/ directory exists
os.makedirs('./datas', exist_ok=True)

def generate_users(num_users=50):
    users = []
    for _ in range(num_users):
        users.append([
            fake.first_name(),
            fake.last_name(),
            fake.email(),
            fake.phone_number(),
            fake.address().replace('\n', ', '),
            fake.date_time_between(start_date='-2y', end_date='now').strftime('%Y-%m-%d %H:%M:%S')
        ])

    with open('./datas/users.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['firstname', 'lastname', 'email', 'phone', 'address', 'registration_date'])
        writer.writerows(users)

def generate_admins(num_admins=5):
    admins = []
    for _ in range(num_admins):
        admins.append([
            fake.name(),
            fake.email(),
            fake.password(length=12)
        ])

    with open('./datas/admins.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['name', 'email', 'password'])
        writer.writerows(admins)

def generate_documents(num_docs=100):
    documents = []
    for _ in range(num_docs):
        documents.append([
            fake.catch_phrase(),  # as title
            fake.name(),          # as author
            fake.text(max_nb_chars=200),  # as description
            fake.company(),       # as publisher
            fake.date_between(start_date='-10y', end_date='today').strftime('%Y-%m-%d'),
            random.randint(1, 5)  # quantity
        ])

    with open('./datas/documents.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['title', 'author', 'description', 'publisher', 'publication_year', 'quantity'])
        writer.writerows(documents)

def generate_borrows(num_borrows=200):
    borrows = []
    for _ in range(num_borrows):
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
            random.randint(1, 50),  # user_id
            random.randint(1, 100), # document_id
            borrow_date.strftime('%Y-%m-%d %H:%M:%S'),
            due_date.strftime('%Y-%m-%d %H:%M:%S'),
            return_date.strftime('%Y-%m-%d %H:%M:%S') if return_date else None,
            round(fine_amount, 2)
        ])

    with open('./datas/borrows.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['user_id', 'document_id', 'borrow_date', 'due_date', 'return_date', 'fine_amount'])
        writer.writerows(borrows)

if __name__ == "__main__":
    generate_users()
    generate_admins()
    generate_documents()
    generate_borrows()
    print("CSV files generated successfully!")