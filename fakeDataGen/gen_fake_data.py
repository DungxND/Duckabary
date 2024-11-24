import csv
import os
import random
from datetime import timedelta
from faker import Faker
import bcrypt

# Constants for file paths and data generation settings.
DATA_DIR = './datas'
USER_FILE = os.path.join(DATA_DIR, 'users.csv')
ADMIN_FILE = os.path.join(DATA_DIR, 'admins.csv')
DOCUMENT_FILE = os.path.join(DATA_DIR, 'documents.csv')
BORROW_FILE = os.path.join(DATA_DIR, 'borrow.csv')

Faker.seed(12345)
random.seed(12345)

fake = Faker()
os.makedirs(DATA_DIR, exist_ok=True)


def write_to_csv(file_path, header, rows):
    """Helper function to write data to CSV files."""
    try:
        with open(file_path, 'w', newline='') as f:
            writer = csv.writer(f)
            writer.writerow(header)
            writer.writerows(rows)
    except IOError as e:
        print(f"Error writing to file {file_path}: {e}")


def generate_users(num_users=100):
    users = []
    for i in range(1, num_users + 1):
        phone_number = '0' + ''.join([str(random.randint(0, 9)) for _ in range(9)])
        users.append([
            i,
            fake.user_name(),
            fake.first_name(),
            fake.last_name(),
            fake.email(),
            phone_number,
            fake.address().replace('\n', ', '),
            fake.date_time_between(start_date='-3y', end_date='now').strftime('%Y-%m-%d %H:%M:%S')
        ])

    write_to_csv(USER_FILE,
                 ['user_id', 'username', 'firstname', 'lastname', 'email', 'phone',
                  'address','registration_date'], users)


def generate_admins(num_admins=5):
    admins = []
    for i in range(1, num_admins + 1):
        hashed_password = bcrypt.hashpw(fake.password(length=12).encode('utf-8'), bcrypt.gensalt()).decode('utf-8')
        admins.append([i, fake.user_name(), fake.email(), hashed_password])

    write_to_csv(ADMIN_FILE,
                 ['manager_id','username','email','hashedPassword'], admins)


def generate_documents(num_docs=1000):
    documents = []
    for i in range(1, num_docs + 1):
        isbn = ''.join([str(fake.random_digit()) for _ in range(13)])  # Corrected syntax
        documents.append([
            i,  # document_id
            fake.catch_phrase(),  # title
            fake.name(),  # author
            fake.text(max_nb_chars=1000),  # description
            fake.company(),  # publisher
            fake.date_between(start_date='-15y', end_date='today').year,  # publish_year
            fake.word(),  # genre
            fake.language_name(),  # language
            isbn,
            random.randint(1, 100)  # quantity
        ])

    with open('./datas/documents.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['document_id', 'title', 'author', 'description', 'publisher', 'publish_year', 'genre', 'language', 'isbn', 'quantity'])
        writer.writerows(documents)

def generate_borrows(num_borrows=80):
    borrows = []
    for i in range(1, num_borrows + 1):
        borrow_date = fake.date_time_between(start_date='-1y', end_date='now')
        due_date = borrow_date + timedelta(days=14)
        return_date = None
        is_returned = False

        # 70% chance the book has been returned
        if random.random() < 0.7:
            return_date = borrow_date + timedelta(days=random.randint(1, 20))
            is_returned = True

        borrows.append([
            i,
            random.randint(1, num_users),  # user_id
            random.randint(1, num_docs),  # document_id
            borrow_date.strftime('%Y-%m-%d %H:%M:%S'),
            due_date.strftime('%Y-%m-%d %H:%M:%S'),
            return_date.strftime('%Y-%m-%d %H:%M:%S') if return_date else None,
            is_returned
        ])

    write_to_csv(BORROW_FILE,
                 ['borrow_id', 'user_id', 'document_id', 'borrow_date', 'due_date',
                  'return_date', 'is_returned'], borrows)


if __name__ == "__main__":
    num_users = 100
    num_docs = 500
    generate_users(num_users)
    generate_admins()
    generate_documents(num_docs)
    generate_borrows()
    print("CSV files generated successfully!")
