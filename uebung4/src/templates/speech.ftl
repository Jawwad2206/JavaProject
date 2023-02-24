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

<h1 style="align-content: center">This is the Speech with the ${ID}</h1>

<div>
    <em style="font-size: large"><b>Name:</b></em> ${name}<br>
    <em style="font-size: large"><b>Partei:</b></em> ${party}<br>
    <em style="font-size: large"><b>Fraktion:</b></em> ${fraction}<br>
    <em style="font-size: large"><b>SpeakerID:</b></em> ${speakerID}<br>
</div>
<div>
    <br><br><em style="font-size: large"><b>Content:</b></em> ${speechContent}
</div>
</body>
</html>