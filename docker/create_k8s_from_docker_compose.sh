#!/bin/bash

rm -rfv ./k8s
mkdir -v ./k8s

kind delete cluster --name kotlin-sample-cluster
kind create cluster --name kotlin-sample-cluster

docker build -t diones/kotlin-sample-user:latest ../user
docker build -t diones/kotlin-sample-card:latest ../card

kind load docker-image diones/kotlin-sample-user:latest --name kotlin-sample-cluster
kind load docker-image diones/kotlin-sample-card:latest --name kotlin-sample-cluster

#K8S_API_SERVER_HOST_PORT=$(kubectl describe service kubernetes | grep Endpoints | awk '{ print $2 }') ### kubernetes apiserver ip and port
#kompose up --push-image=false --server https://"${K8S_API_SERVER_HOST_PORT}"

kompose convert -o ./k8s && kubectl create -f ./k8s

