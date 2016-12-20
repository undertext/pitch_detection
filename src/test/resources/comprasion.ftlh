<html>
<head>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>

</head>
<body>

<#list algs as alg>
<div class="panel panel-default">
    <div class="panel-heading">${alg.class}</div>
        <#list alg.res as key,fftResultForNote>
           <div class="panel-heading">${key}</div>
    <table class="table">
        <thead>
        <tr>
            <th>Chunk number</th>
            <th>Original Frequency</th>
            <th>Detected Frequency</th>
            <th>Deviation</th>
            <th>Deviation in percents</th>
        </tr>
        </thead>
        <tbody>
    <#list fftResultForNote as res>
            <tr>
             <th scope="row">${res.chunk}</th>
                <td>${res.original}</td>
                <td>${res.detected}</td>
                <td>${res.deviation}</td>
                <td>${res.deviationPercents}</td>
            </tr>
            </#list>
        </tbody>
    </table>
        <div class="panel-footer">Overall deviation  <b> ${alg.deviations[key]}</b></div>
       </#list>
</div>

<b> Overall deviation ${alg.overallDeviation} </b>
 </#list>


</body>

</html>
