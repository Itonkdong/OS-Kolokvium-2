FROM debian:11-slim
RUN apt-get update && apt-get upgrade -y && apt-get install -y nginx curl && rm -rf /var/lib/apt/lists/*
CMD ["nginx", "-g", "daemon off;"]


# docker run --name nginx-product-host-i-container --rm -p 8080:80 -it nginx-product-host-i bash
# docker run --name nginx-product-host-container -p 8081:80 -d --rm nginx-product-host

#  docker exec nginx-product-host-i-container ls /var/www/html/

# curl.exe -O http://localhost:8081/products.txt
# curl.exe -O http://localhost:8080/products.txt

# docker run -d --name nginx-product-host-container --rm -v .\html:/var/www/html/ -p 8081:80 nginx-product-host