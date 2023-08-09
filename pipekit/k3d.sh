#!/bin/bash

k3d cluster create --config k3d.conf

# Prevent users from accidentally deploying to the wrong cluster.
currentContext=$(kubectl config current-context)
if [ "$currentContext" == "k3d-pipekit-spaceboot" ]; then
    echo "Starting deployment to cluster..."
else
    echo "The kubectl context is not what we expected. Exiting for safety. Perhaps the k3d cluster failed to create?"
    exit 1
fi

# Install Argo Workflows
kubectl create namespace argo
kubectl apply -n argo -k pre-requisites/argo-workflows

# Install NFS Server Provisioner
kubectl create namespace nfs-server-provisioner
kubectl config set-context --current --namespace=nfs-server-provisioner
helm template nfs pre-requisites/nfs-server-provisioner -f pre-requisites/nfs-server-provisioner/values.yaml | kubectl apply -f -
kubectl config set-context --current --namespace=default

# Check for healthiness
kubectl -n nfs-server-provisioner rollout status statefulset/nfs-nfs-server-provisioner
kubectl -n argo rollout status deployment/workflow-controller
kubectl -n argo rollout status deployment/argo-server
