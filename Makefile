# Common makefile structure for buildable go projects that result in a docker container artifact
export SHELL:=/bin/bash
export SHELLOPTS:=$(if $(SHELLOPTS),$(SHELLOPTS):)pipefail:errexit

CWD=$(shell pwd)

export VERSION?=$(shell git describe --always)

# for docker image tagging and repos
export IMAGE_NAME?=spaceboot
export PLATFORMS?=linux/amd64,linux/arm64
export REGISTRY?=gcr.io/speedscale-demos

export REPO=$(REGISTRY)/$(IMAGE_NAME)

all: build

build:
	docker buildx build \
		-f $(CWD)/Dockerfile \
		$(if $(CI),--platform $(PLATFORMS) --push,) \
		--tag $(REPO)$(IMAGE_SUFFIX):$(VERSION) \
		--build-arg VERSION=$(VERSION) \
		$(CWD)
	docker tag $(REPO)$(IMAGE_SUFFIX):$(VERSION) $(REPO)$(IMAGE_SUFFIX):latest
	docker push $(REPO)$(IMAGE_SUFFIX):latest
