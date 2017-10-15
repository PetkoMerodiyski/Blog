$(document).ready(function () {
// Get the modal
    var modal = document.getElementById('loginModal');

// Get the button that opens the modal
    var btn = document.getElementById("loginTest");

// When the user clicks the button, open the modal
    btn.onclick = function() {
        modal.style.display = "block";
    }

// When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
});
