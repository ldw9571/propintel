━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
대시보드
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GET  /api/v1/dashboard/summary
→ 전국 KPI (평균가, 거래량, 전세가율)

GET  /api/v1/dashboard/price-index?area=ALL|SEOUL|METRO|LOCAL
→ 월별 가격지수 시계열

GET  /api/v1/dashboard/volume?months=12
→ 월별 거래량 추이

GET  /api/v1/dashboard/top-regions?limit=5
→ 상승률 TOP 지역

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
지역 분석
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GET  /api/v1/regions
GET  /api/v1/regions/{code}
GET  /api/v1/regions/{code}/stats?from=2015-01&to=2025-06
GET  /api/v1/regions/{code}/supply?years=3
GET  /api/v1/regions/{code}/population
GET  /api/v1/regions/{code}/jobs

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
단지 분석
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GET  /api/v1/complexes?regionCode=&name=&page=0&size=20
GET  /api/v1/complexes/{id}
GET  /api/v1/complexes/{id}/transactions?type=SALE&from=&to=
GET  /api/v1/complexes/{id}/price-chart
GET  /api/v1/complexes/{id}/similar

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
투자 점수
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GET  /api/v1/scores/complex/{id}
POST /api/v1/scores/complex/{id}/refresh

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
AI 리포트
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GET  /api/v1/reports/complex/{id}
POST /api/v1/reports/complex/{id}/generate

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
지도
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GET  /api/v1/map/complexes?lat=&lng=&radius=3000
GET  /api/v1/map/hotspots?regionCode=
GET  /api/v1/map/subway-stations