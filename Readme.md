First Steps

1. Created SB APP
2. Created Dockerfile
3. Ran docker build -t todolist .
4. docker run -p 8080:8080 todolist


Now github actions

Repo -> Settings -> Secrets n vars -> actions

Generate access token on docker hub


name: Maven Package

on:
  push:
    branches: [main]
    paths:
      - todolist/**

jobs:
  build:

    runs-on: ubuntu-latest
    
    outputs:
      image_tag: ${{ github.sha }}

    steps:
    - uses: actions/checkout@v4
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Build with Maven
      working-directory: todolist
      run: mvn clean package -DskipTests

    - name: Login to Docker Hub
      run: echo "${{ secrets.DOCKER_PASSWORD }}" | docker login -u "${{ secrets.DOCKER_USERNAME }}" --password-stdin

    - name: Build Docker Image
      working-directory: todolist
      run: docker build -t ${{ secrets.DOCKER_USERNAME }}/todolist:${{ github.sha }} .

    - name: Push Docker Image
      working-directory: todolist
      run: docker push ${{ secrets.DOCKER_USERNAME }}/todolist:${{ github.sha }}


  update-deploy-repo:
    runs-on: ubuntu-latest
    needs: build

    steps:
      - name: Clone deployment repo
        run: |
          git clone https://${{ secrets.DEPLOY_REPO_TOKEN }}@github.com/Peaksis1/Kubernetes-Helm-AgroCD-Devops.git
          cd Kubernetes-Helm-AgroCD-Devops

      - name: Update Helm values.yaml
        run: |
          cd YOUR_DEPLOY_REPO/helm-chart
          sed -i "s/tag:.*/tag: ${{ needs.build.outputs.image_tag }}/" values.yaml

      - name: Commit and push changes
        run: |
          cd YOUR_DEPLOY_REPO
          git config user.name "github-actions"
          git config user.email "actions@github.com"
          git add .
          git commit -m "Update image tag to ${{ needs.build.outputs.image_tag }}"
          git push



NOW AGROCD

In other repo



NOTE:

Need to run
1. Docker desktop
2. Minikube