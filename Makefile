SERVICE_NAME=hello

# Docker Hub username.
# ?= inseamna: foloseste valoarea asta default doar daca nu este deja setata din afara.
# Exemplu:
# make build DOCKER_USERNAME=altuser
DOCKER_USERNAME?=williamlazar2002

# Numele complet al imaginii Docker finale.
# In loc de hello-img simplu local, acum imaginea va fi:
# williamlazar2002/hello-img
IMAGE_NAME=$(DOCKER_USERNAME)/hello-img

# Versiunea aplicatiei.
# Aceasta versiune va fi folosita:
# 1. la build-ul JAR-ului
# 2. la tag-ul imaginii Docker finale
#
# Exemplu:
# make build APP_VERSION=0.0.2
APP_VERSION?=0.0.1-SNAPSHOT

# Imagine temporara folosita doar pentru build.
# Aceasta NU este imaginea finala de deploy.
# Ea contine tool-urile necesare pentru build, de exemplu Gradle/Maven/JDK.
BUILDER_TAG=$(SERVICE_NAME)-build

# Container temporar pornit din imaginea de build.
# Acest container compileaza aplicatia si genereaza artifact-ul, adica JAR-ul.
BUILDER_CONTAINER_NAME=$(SERVICE_NAME)-builder-container

# Target-ul default.
# Cand rulezi simplu:
# make
# se va executa target-ul build.
default: build

clean:
	# Sterge folderul build generat anterior.
	# Aici se afla de obicei artifact-ul generat, adica JAR-ul.
	rm -rf build

	# Sterge fortat containerul temporar de build, daca exista.
	# || true inseamna: daca nu exista, nu opri executia cu eroare.
	docker rm -f $(BUILDER_CONTAINER_NAME) || true

	# Sterge imaginea temporara de build.
	docker rmi $(BUILDER_TAG) || true

	# Sterge imaginea finala a aplicatiei pentru versiunea curenta.
	# Exemplu:
	# docker rmi williamlazar2002/hello-img:0.0.1-SNAPSHOT
	docker rmi $(IMAGE_NAME):$(APP_VERSION) || true

	# Sterge imaginile dangling, adica imaginile fara tag.
	# Apar des dupa rebuild-uri Docker.
	docker image prune -f

build: clean
	# Construieste imaginea temporara de build folosind Dockerfile.build.
	# Aceasta imagine este folosita doar ca mediu izolat de compilare.
	#
	# Avantaj:
	# Nu ai nevoie sa ai toate dependintele instalate direct pe masina ta.
	# Build-ul se face intr-un container Docker.
	docker build -t $(BUILDER_TAG) -f Dockerfile.build .

	# Porneste containerul temporar de build.
	# Acest container compileaza aplicatia si genereaza JAR-ul.
	#
	# -e APP_VERSION=$(APP_VERSION)
	# Trimite versiunea aplicatiei in container.
	# Asa build-ul poate genera artifact-ul cu aceeasi versiune peste tot.
	#
	# -v "m2:/root/.m2"
	# Monteaza un volum Docker pentru cache-ul Maven.
	# Astfel dependintele Maven nu se descarca de la zero la fiecare build.
	#
	# -v "`pwd`:/build"
	# Monteaza proiectul curent in container, in folderul /build.
	# Containerul lucreaza pe codul tau actual.
	docker run --name $(BUILDER_CONTAINER_NAME) \
		-e APP_VERSION=$(APP_VERSION) \
		-v "m2:/root/.m2" \
		-v "`pwd`:/build" \
		$(BUILDER_TAG)

	# Opreste si sterge containerul temporar de build.
	# Containerul nu mai este necesar dupa ce JAR-ul a fost generat.
	docker stop $(BUILDER_CONTAINER_NAME) || true
	docker rm $(BUILDER_CONTAINER_NAME) || true

	# Construieste imaginea Docker finala a aplicatiei.
	#
	# --build-arg APP_VERSION=$(APP_VERSION)
	# Trimite versiunea catre Dockerfile.
	# Dockerfile poate folosi aceasta versiune ca sa copieze JAR-ul corect.
	#
	# -t $(IMAGE_NAME):$(APP_VERSION)
	# Pune tag pe imaginea finala.
	#
	# Exemplu rezultat:
	# williamlazar2002/hello-img:0.0.1-SNAPSHOT
	docker build \
		--build-arg APP_VERSION=$(APP_VERSION) \
		-t $(IMAGE_NAME):$(APP_VERSION) .

	# Mesaj final de confirmare.
	echo "Success: built $(IMAGE_NAME):$(APP_VERSION)"

post-deploy-build:
	# Momentan nu exista pasi definiti dupa deploy/build.
	echo "Nothing is defined in post-deploy-build step"