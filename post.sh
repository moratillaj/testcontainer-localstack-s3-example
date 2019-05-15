curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"key": "greeting.txt", "text":"Hello localstack!!"}' \
  http://localhost:9090/textContent
