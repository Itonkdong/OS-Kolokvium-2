FROM eclipse-temurin
WORKDIR /var/www/html/
WORKDIR /app
COPY ProductsManagement.java .
RUN javac ProductsManagement.java
CMD ["java", "ProductsManagement"]


# docker run --rm --name products-viewer-container -v .\html\:/var/www/html/  products-viewer
# docker run --rm --name products-viewer-container -v .\html\:/var/www/html/ -e PRODUCT_FILE_PATH=/var/www/html/products.txt  products-viewer
