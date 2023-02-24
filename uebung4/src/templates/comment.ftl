<html>

<meta><title>${ID}</title></meta>

<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
<form>
    <label for="fname">SpeechID:</label><br>
    <input type="text" id="speakerId" name="speakerId"><br>
    <input type="submit" value="Apply Filter">
</form>

<h1 style="align-content: center">These are comments of the Speech with the ID -> ${ID}</h1>
<div>
   <p><em style="font-size: large"><b>Kommentare:</b></em> <br><#list comments as comment>${comment}<#sep><br></#list></p>
</div>
</body>
</html>