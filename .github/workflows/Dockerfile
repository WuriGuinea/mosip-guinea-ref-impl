FROM ubuntu:xenial


# Install required system packages and dependencies
RUN apt-get update && apt-get upgrade -y && \
RUN apt install software-properties-common && \
    sudo add-apt-repository ppa:ondrej/php && \

    rm -r /var/lib/apt/lists /var/cache/apt/archives && apt-get install git
RUN mkdir /app && chown bitnami:bitnami /app

COPY rootfs /
ENV BITNAMI_APP_NAME="laravel" \
    BITNAMI_IMAGE_VERSION="6.20.0-debian-10-r14" \
    NODE_PATH="/opt/bitnami/node/lib/node_modules" \
    OS_ARCH="amd64" \
    OS_FLAVOUR="debian-10" \
    OS_NAME="linux"

WORKDIR /app
USER bitnami
RUN git clone https://github.com/sanz/laravel-inventory.git .
CMD [ "php", "artisan", "serve", "--host=0.0.0.0", "--port=4000" ]

###################################
FROM jorge07/alpine-php:7.3
WORKDIR /appli
RUN apk add git && apk add curl
RUN git clone https://github.com/sanz/laravel-inventory.git .
RUN  apk add composer && composer install
COPY .env /appli
RUN php artisan migrate 
RUN    php artisan key:generate 
RUN    php artisan storage:link 
EXPOSE 4000
CMD ["php", "artisan", "serve", "--port=4000", "--host=0.0.0.0"]


      - name: Push image
        run: |
          IMAGE_ID=$NAMESPACE/$SERVICE_NAME

          # Change all uppercase to lowercase
          IMAGE_ID=$(echo $IMAGE_ID | tr '[A-Z]' '[a-z]')
          echo "push version ${{steps.getPomVersion.outputs.info}}"
          VERSION=$BRANCH_NAME
          echo IMAGE_ID=$IMAGE_ID
          echo VERSION=$VERSION
          docker tag $SERVICE_NAME $IMAGE_ID:$VERSION
          docker push $IMAGE_ID:$VERSION