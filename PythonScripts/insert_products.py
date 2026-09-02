import requests
import random

BASE_URL = "http://localhost:8082/api/products"

categories = [
    "Electronics",
    "Furniture",
    "Books",
    "Clothing",
    "Home Appliances",
    "Sports",
    "Accessories"
]

products = [
    "Laptop",
    "Smartphone",
    "Monitor",
    "Keyboard",
    "Mouse",
    "Headphones",
    "Office Chair",
    "Table",
    "Backpack",
    "Running Shoes",
    "Smart Watch",
    "Camera",
    "Speaker",
    "Tablet",
    "Desk Lamp"
]


def create_product(index):
    category = random.choice(categories)
    product_name = random.choice(products)

    payload = {
        "name": f"{product_name} {index}",
        "description": f"Product {index} for ProductHub performance testing",
        "price": round(random.uniform(100, 100000), 2),
        "category": category,
        "imageUrl": f"https://example.com/products/{index}.jpg"
    }

    response = requests.post(
        BASE_URL,
        json=payload,
        timeout=10
    )

    return response


def main():
    try:
        count = int(input("How many products do you want to create? "))

        if count <= 0:
            print("Please enter a number greater than 0.")
            return

    except ValueError:
        print("Please enter a valid number.")
        return

    success = 0
    failed = 0

    print(f"\nCreating {count} products...\n")

    for i in range(1, count + 1):

        try:
            response = create_product(i)

            if response.status_code == 201:
                success += 1
                print(f"[{i}/{count}] Created successfully")

            else:
                failed += 1
                print(
                    f"[{i}/{count}] Failed - "
                    f"HTTP {response.status_code}: {response.text}"
                )

        except requests.exceptions.RequestException as e:
            failed += 1
            print(f"[{i}/{count}] Request failed: {e}")

    print("\n-----------------------------")
    print("Insertion completed")
    print("-----------------------------")
    print(f"Successful : {success}")
    print(f"Failed     : {failed}")
    print(f"Total      : {count}")


if __name__ == "__main__":
    main()