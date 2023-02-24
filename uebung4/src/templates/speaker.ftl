<html>

<meta><title>${title}</title></meta>

<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
<form>
    <label for="fname">Speaker ID:</label><br>
    <input type="text" id="speakerId" name="speakerId"><br>
    <input type="submit" value="Apply Filter">
</form>

<h1 style="align-content: center">This is the Speaker List, which contains all Deputies, who were part of the Parliament.</h1>

<br><br>
<div>
    <ul>
        <#list speakers as speaker>
            <br><br><em style="font-size: large">Firstname:</em>  <b>${speaker.getFirstName()}</b> <em style="font-size: large">Lastname:</em> <b>${speaker.getLastName()}</b>  <em style="font-size: large">SpeakerID:</em> <b>${speaker.getID()}</b>  <em style="font-size: large">Party:</em> <b>${speaker.getParty()}</b>  <em style="font-size: large">Fraktion:</em> <b>${speaker.getFraction()}</b><br>
        </#list>
    </ul>
</div>
</body>
</html>