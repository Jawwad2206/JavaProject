<!DOCTYPE html>
<html lang="en">
<head>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.min.js"></script>
</head>
<body>

<div style="width: 40%;">
    <h1  style="font-size: 2em;">Verteilung des Sentiment in den Reden.</h1>
    <canvas id="posDiagramm" width="400" height="400" ></canvas>
    <h1  style="font-size: 2em;">Verteilung des Sentiment in den Kommentaren.</h1>
    <canvas id="commentSentiment" width="400" height="400", style="height: 300px" ></canvas>
    <h1  style="font-size: 2em;">Häufigkeitsverteilung der Lemmata in allen Kommentaren.</h1>
    <canvas id="myChart" width="400" height="400"></canvas>
    <h1  style="font-size: 2em;">Häufigkeit der Nennung aller Named Entities in den Reden, sortiert nach Typ der
        Named Entity </h1>
    <canvas id="BarChart" width="400" height="400"></canvas>
</div>

<script>
    const array_name = [${json.get("postive")}, ${json.get("negativ")}, ${json.get("neutral")}];
    const data = {
        labels: [
            'positive',
            'negativ',
            'neutral'
        ],
        datasets: [{
            label: 'My First Dataset',
            data: array_name,
            backgroundColor: [
                'rgb(255, 99, 132)',
                'rgb(54, 162, 235)',
                'rgba(255, 205, 86, 0.2)',
            ],
            hoverOffset: 4
        }]
    };
    const config = {
        type: 'doughnut',
        data: data,
    };
    var canvasPOS = document.getElementById("posDiagramm")
    globalThis.posDia = new Chart(canvasPOS, config);

</script>
<script>
    const array_name_Comment = [${comments.get("postive")}, ${comments.get("negativ")}, ${comments.get("neutral")}];
    const data_Comment = {
        labels: [
            'positive',
            'negativ',
            'neutral'
        ],
        datasets: [{
            label: 'My First Dataset',
            data: array_name_Comment,
            backgroundColor: [
                'rgba(153, 102, 255, 0.2)',,
                'rgba(255, 205, 86, 0.2)',
                'rgb(255, 205, 86)'
            ],
            hoverOffset: 4
        }]
    };
    const config_comment = {
        type: 'doughnut',
        data: data_Comment,
    };
    var canvasPOS_Comment = document.getElementById("commentSentiment")
    globalThis.posDia_Comment = new Chart(canvasPOS_Comment, config_comment);
</script>
<script>
    async function getData(url = "")
    {
        const response = await fetch(url);
        return response.json();
    }

    let NOUN = ${NOUN.get("allNOUN.k")}
    let DET = ${DET.get("allNOUN.k")}
    let ADV = ${ADV.get("allNOUN.k")}
    let VERB = ${VERB.get("allNOUN.k")}
    let PROPN = ${PROPN.get("allNOUN.k")}
    let PUNCT = ${PUNCT.get("allNOUN.k")}
    let ADJ = ${ADJ.get("allNOUN.k")}
    let NUM = ${NUM.get("allNOUN.k")}
    let X = ${X.get("allNOUN.k")}
    let ADP = ${ADP.get("allNOUN.k")}

    const ctx = document.getElementById('myChart');

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ["NOUN","DET", "ADV","VERB" , "PROPN", "PUNCT", "ADJ","NUM","X", "ADP"],
            datasets: [{
                label: "All Lemmata of Comments",
                data: [NOUN, DET, ADV,VERB , PROPN, PUNCT, ADJ,NUM,X, ADP],
                borderWidth: 1,
                backgroundColor: [
                    'rgb(255, 99, 132)',
                    'rgb(54, 162, 235)',
                    'rgb(255, 205, 86)',
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(255, 159, 64, 0.2)',
                    'rgba(255, 205, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(153, 102, 255, 0.2)',
                    'rgba(201, 203, 207, 0.2)'
                ],
                hoverOffset: 4
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

</script>
<script>
    async function getData(url = "")
    {
        const response = await fetch(url);
        return response.json();
    }

    let Pers = ${Pers.get("persons.k")}
    let Org = ${Org.get("persons.k")}
    let MISC = ${MISC.get("persons.k")}
    let LOC = ${LOC.get("persons.k")}

    const ctxx = document.getElementById('BarChart');

    new Chart(ctxx, {
        type: 'bar',
        data: {
            labels: ["Pers","Org", "MISC", "LOC"],
            datasets: [{
                label: "All Lemmata of Comments",
                data: [Pers, Org, MISC,LOC],
                borderWidth: 1,
                backgroundColor: [
                    'rgba(54, 162, 235, 0.2)',
                    'rgb(54, 162, 235)',
                    'rgb(255, 205, 86)',
                    'rgba(255, 99, 132, 0.2)'
                ],
                hoverOffset: 4
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

</script>
</body>
</html>