if (window.location.pathname.includes("Game.html")) {
const miBody = document.getElementById("bodyGame");
miBody.onload = function() {
  animarCartas();
};
}

/*
const tablero = document.querySelector('#tablero');
const tablero2 = document.querySelector('#tableroRival');

tablero.addEventListener('click', () => {
  tablero.classList.remove('tablero-activo');
  tablero.classList.add('tablero-oculto');
  tableroRival.classList.remove('tablero-oculto');
  tableroRival.classList.add('tablero-activo');
});
tablero2.addEventListener('click', () => {
  tableroRival.classList.remove('tablero-activo');
  tableroRival.classList.add('tablero-oculto');
  tablero.classList.remove('tablero-oculto');
  tablero.classList.add('tablero-activo');
});
*/
function girarCarta(id) {
    var carta = document.getElementById(id);
    carta.classList.toggle('girada');
  
    setTimeout(function() {
      carta.getElementsByClassName('carta-frontal')[0].classList.toggle('oculta');
      carta.getElementsByClassName('carta-trasera')[0].classList.toggle('show');
    }, 500);
  }

  function girarCartaSecreta(id) {
    var carta = document.getElementById(id);
    carta.classList.toggle('girada');
  
    setTimeout(function() {
      carta.getElementsByClassName('carta-frontal')[0].classList.toggle('oculta');
      carta.getElementsByClassName('carta-trasera')[0].classList.toggle('show');
    }, 500);
  }
  function cartaElegida(id) {
    clearInterval(intervalo);
    var cartaId = id.getAttribute('id');
    var cartaElegida = document.getElementById(cartaId);
    var cartas = document.querySelectorAll('.carta');
    cartas.forEach(function(carta) {
      carta.classList.remove('animar');
      carta.classList.remove('elegida');
      carta.style.zIndex = 1; 
    });
    cartaElegida.classList.toggle('elegida');
    cartaElegida.style.zIndex = 10; 



    var src =cartaElegida.querySelector(".carta-frontal img").getAttribute("src");

    var cartaSecreta = document.getElementById("carta-secreta");
    cartaSecreta.querySelector(".carta-trasera img").setAttribute("src", src);
  }
  //Se declara el intervalo de forma global para poder eliminarlo cuando necesite
  var intervalo;
  function animarCartas() {
    var cartas = document.querySelectorAll('.carta');
    var cartaGirada = document.querySelector('.carta.girada');
  
    // Generar un retraso aleatorio para cada carta
    cartas.forEach(function(carta) {
      var retraso = Math.floor(Math.random() * 1000); // Generar un retraso aleatorio entre 0 y 1000 ms
      carta.setAttribute('data-retraso', retraso);
    });
  
    intervalo = setInterval(function() {
      
  
      cartas.forEach(function(carta) {
        var retraso = parseInt(carta.getAttribute('data-retraso')); // Obtener el retraso aleatorio para esta carta
        setTimeout(function() {
          carta.classList.toggle('animar');
        }, retraso);
      });
  
      cartaGirada = document.querySelector('.carta.girada');
    }, 650);
  }
  function elegirPersonaje(personaje) {

    var src = personaje.querySelector("img").getAttribute("src");

    var divSeleccionado = document.getElementById("div-selected");
    divSeleccionado.querySelector("img").setAttribute("src", src);

    divSeleccionado.style.display = "block";

    var formPlay = document.getElementById("formSelect");
    formPlay.style.display = "block";
}

// Obtener referencia al formulario "formPlay"
var formPlay = document.getElementById("formPlay");

// Asignar un evento submit al formulario
formPlay.addEventListener("submit", function(event) {
    // Evitar que la página se recargue al enviar el formulario
    event.preventDefault();

    // Crear una instancia del objeto XMLHttpRequest
    var xhr = new XMLHttpRequest();

    // Configurar la solicitud HTTP
    xhr.open("POST", "/playgame", true);

    // Enviar la solicitud sin datos adicionales
    xhr.send();
    animarCartas();
});

var formPlay = document.getElementById("formSelect");

formPlay.addEventListener("submit", function(event) {
    event.preventDefault();
    
    var cartaFrontal = document.querySelector('#div-selected .carta-frontal img');
    var srcCartaFrontal = cartaFrontal.getAttribute('src');
    
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/selectCharacter?srcCartaFrontal=" + srcCartaFrontal, true);
    xhr.send();

});

//Formulario
var mostrarFormularioBtn = document.getElementById('mostrar-formulario');
var formularioContainer = document.getElementById('formulario-container');
var enviarBtn = document.getElementById('miFormulario').querySelector('button[type="submit"]');

// Mostrar el formulario al hacer clic en el botón
mostrarFormularioBtn.addEventListener('click', function() {
  formularioContainer.style.display = 'block';
  formularioContainer.classList.add('animate__animated', 'animate__slideInLeft');
});

// Ocultar el formulario al hacer clic en el botón Enviar
enviarBtn.addEventListener('click', function(e) {
  e.preventDefault();
  formularioContainer.classList.remove('animate__slideInLeft');
  formularioContainer.classList.add('animate__slideOutLeft');
  formularioContainer.addEventListener('animationend', function() {
    if (formularioContainer.classList.contains('animate__slideOutLeft')) {
    formularioContainer.classList.remove('animate__slideOutLeft');
    formularioContainer.style.display = 'none';
    }
  });

  // Obtener los valores de los campos del formulario
  const opciones1 = form.opciones1.value;
  const opciones2 = form.opciones2.value;
  const opciones3 = form.opciones3.value;

  // Crear una nueva solicitud AJAX
  const xhr = new XMLHttpRequest();

  // Configurar la solicitud AJAX
  xhr.open("POST", "/guess");
  xhr.setRequestHeader("Content-Type", "application/json");

  // Manejar la respuesta de la solicitud AJAX
  xhr.onload = function() {
    if (xhr.status === 200) {
      // La solicitud se ha completado correctamente
      var scriptElement = document.createElement("script");
    scriptElement.textContent = xhr.responseText;
    document.body.appendChild(scriptElement);
    } else {
      // Ha ocurrido un error durante la solicitud
      console.error(xhr.statusText);
    }
  };

  // Enviar la solicitud AJAX con los datos del formulario
  xhr.send(JSON.stringify({
    opciones1: opciones1,
    opciones2: opciones2,
    opciones3: opciones3
  }));
});

document.getElementById('miFormulario').addEventListener('submit', function(event) {
  event.preventDefault(); // Previene la acción por defecto del evento submit
  
  var container = document.getElementById('formulario-container');
  container.classList.add('animate__animated');
  container.classList.add('animate__slideOut');
  
  container.addEventListener('animationend', function() {
    container.style.display = 'none';
  });
});

// Obtener el formulario y el botón de enviar
const form = document.getElementById("miFormulario");
const submitButton = form.querySelector("button[type=submit]");

document.addEventListener("DOMContentLoaded", function() {
  var opciones1 = document.getElementById("opciones1");
  var opciones2 = document.getElementById("opciones2");
  var opciones3 = document.getElementById("opciones3");
  opciones1.addEventListener("change", function() {
    var seleccion = opciones1.value;
    const opcionSombrero = opciones3.querySelector("option[value='1']");
    opcionSombrero.removeAttribute("hidden");
    for (var i = 0; i < opciones3.options.length; i++) {
      if (opciones3.options[i].value == seleccion) {
        opciones3.options[i].style.display = "none";
        if (opciones3.selectedIndex == i) {
          if(i == 0){
            opciones3.selectedIndex = 1;
          }
          else
          {
            opciones3.selectedIndex = 0;
          }
          
        }
      } else {
        opciones3.options[i].style.display = "block";
      }
    }
  });
  
  opciones2.addEventListener("change", function() {
    if (opciones2.value == "n") {
      opciones3.style.display = "none";
      opciones3.selectedIndex = 0;
    } else {
      opciones3.style.display = "block";
      for (var i = 0; i < opciones3.options.length; i++) {
        var seleccion = opciones1.value;
        if (opciones3.options[i].value != seleccion) {
          opciones3.options[i].style.display = "block";
        }else{
          if(i == 0){
            opciones3.selectedIndex = 1;
          }
        }
        
      }
    }
  });
});

/*Boton de confirmar seleccion*/

function mostrarBotonSeleccion() {
  const boton = document.querySelector('.confirmar-seleccion');
  setTimeout(() => boton.classList.add('mostrarBoton'), 100);
}

function ocultarBotonSeleccion() {
  const boton = document.querySelector('.confirmar-seleccion');

  setTimeout(() => boton.classList.add('ocultarBotonClick'), 100);
  girarCartaSecreta('carta-secreta');

}