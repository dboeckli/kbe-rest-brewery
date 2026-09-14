cd target/helm/repo

$file = Get-ChildItem -Filter kbe-rest-brewery-chart-*.tgz | Select-Object -First 1
$APPLICATION_NAME = Get-ChildItem -Directory | Where-Object { $_.LastWriteTime -ge $file.LastWriteTime } | Select-Object -ExpandProperty Name
Write-Host "test application: $APPLICATION_NAME"
helm test $APPLICATION_NAME --namespace kbe-rest-brewery --logs

kubectl delete pod -n kbe-rest-brewery --field-selector=status.phase==Succeeded
kubectl delete pod -n kbe-rest-brewery --field-selector=status.phase==Failed
