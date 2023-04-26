
function iniciarJuego() {
  const pantallaInicio = document.getElementById('pantalla-inicio');
  const fondoDifuminado = document.getElementById('fondo-difuminado');
  reiniciarTablero();
  pantallaInicio.style.animation = 'fadeOut 1s forwards';
  fondoDifuminado.style.animation = 'fadeOut 1s forwards';

  setTimeout(() => {
    pantallaInicio.style.display = 'none';
    fondoDifuminado.style.display = 'none';

    const fakeEvent = new Event('fake'); // Crear un evento falso
    startGame(fakeEvent); // Llamar a la función startGame pasando el evento falso
  }, 1500);
}

function startGame(event) {
  // Evitar que la página se recargue al enviar el formulario
  event.preventDefault();

  // Crear una instancia del objeto XMLHttpRequest
  var xhr = new XMLHttpRequest();

  // Configurar la solicitud HTTP
  xhr.open("POST", "/playgame", true);

  // Enviar la solicitud sin datos adicionales
  xhr.send();
  animarCartas();
  showMensajeInfoElegir();

}

function mostrarPantallaInicio(ganador) {
  if(ganador ==1){
    const header = document.querySelector('h1');
    header.innerHTML = '¡Victoria!';
  }else{
    const header = document.querySelector('h1');
    header.innerHTML = '¡Derrota!';
  }

  
  const button = document.querySelector('#btn-empezar');
  button.innerHTML = 'Volver a jugar';

  const pantallaInicio = document.getElementById('pantalla-inicio');
  const fondoDifuminado = document.getElementById('fondo-difuminado');

  pantallaInicio.style.animation = 'fadeIn 1s forwards';
  fondoDifuminado.style.animation = 'fadeIn 1s forwards';
  pantallaInicio.style.display = 'block';
  fondoDifuminado.style.display = 'block';
  if(ganador == 2){
    setTimeout(() => {
      cambiarTablero();
    }, 2000);
  }
  setTimeout(() => {
    pantallaInicio.style.animation = '';
    fondoDifuminado.style.animation = '';
  }, 1000);
}
/* Funcion para cambiar los tableros */
function cambiarTablero() {
  const tablero = document.querySelector('#tablero');
  const tableroRival = document.querySelector('#tableroRival');

  if (tablero.classList.contains('tablero-activo')) {
    tablero.classList.remove('tablero-activo');
    tablero.classList.add('tablero-oculto');
    tableroRival.classList.remove('tablero-oculto');
    tableroRival.classList.add('tablero-activo');
  } else {
    tableroRival.classList.remove('tablero-activo');
    tableroRival.classList.add('tablero-oculto');
    tablero.classList.remove('tablero-oculto');
    tablero.classList.add('tablero-activo');
  }
}
function girarCarta(id) {
  var carta = document.getElementById(id);
  carta.classList.toggle('girada');

  setTimeout(function () {
    carta.getElementsByClassName('carta-frontal')[0].classList.toggle('oculta');
    carta.getElementsByClassName('carta-trasera')[0].classList.toggle('show');
  }, 500);
}

function girarCartaSecreta(id) {
  var carta = document.getElementById(id);
  carta.classList.toggle('girada');

  setTimeout(function () {
    carta.getElementsByClassName('carta-frontal')[0].classList.toggle('oculta');
    carta.getElementsByClassName('carta-trasera')[0].classList.toggle('show');
  }, 500);
}

function comprobarVictoria(numeroEquipo) {
  var giradas = 0;
  
  if(numeroEquipo == 1){
    for (var i = 1; i <= 24; i++) {
      var id = "carta-" + i;
      var carta = document.getElementById(id);
      if (carta.classList.contains("girada")) {
          giradas++;        
      }
    }
  }else{
    for (var i = 25; i <= 48; i++) {
      var id = "carta-" + i;
      var carta = document.getElementById(id);
      if (carta.classList.contains("girada")) {
          giradas++;        
      }
    }
  }
  
  return giradas == 23;
}

function reiniciarTablero(){
// Seleccionar todas las etiquetas div con la clase carta-frontal
var cartas = document.querySelectorAll('.carta');
const boton = document.querySelector('.confirmar-seleccion');
boton.classList.remove('mostrarBoton');
boton.classList.remove('ocultarBotonClick');
// Iterar sobre la lista de etiquetas y añadir el atributo onclick
cartas.forEach(function(carta) {
    var cartaFrontal = carta.querySelector('.carta-frontal');
    var cartaTrasera = carta.querySelector('.carta-trasera');
    cartaFrontal.setAttribute('onclick', 'cartaElegida(this.closest(\'.carta\'))');
    carta.setAttribute('onclick','mostrarBotonSeleccion()');
    carta.classList.remove('girada');
    cartaFrontal.classList.remove('oculta');
    cartaTrasera.classList.remove('show');                 
});

}
//Se declara el intervalo de forma global para poder eliminarlo cuando necesite
var intervalo;
function animarCartas() {
  var cartas = document.querySelectorAll('.carta');
  var cartaGirada = document.querySelector('.carta.girada');

  // Generar un retraso aleatorio para cada carta
  cartas.forEach(function (carta) {
    var retraso = Math.floor(Math.random() * 1000); // Generar un retraso aleatorio entre 0 y 1000 ms
    carta.setAttribute('data-retraso', retraso);
  });

  intervalo = setInterval(function () {


    cartas.forEach(function (carta) {
      var retraso = parseInt(carta.getAttribute('data-retraso')); // Obtener el retraso aleatorio para esta carta
      setTimeout(function () {
        carta.classList.toggle('animar');
      }, retraso);
    });

    cartaGirada = document.querySelector('.carta.girada');
  }, 650);
}

//Formulario
var formularioContainer = document.getElementById('formulario-container');
var enviarBtn = document.getElementById('miFormulario').querySelector('button[type="submit"]');
/* Desplegable de preguntas */
function showDesplegablePreguntas() {
  // Mostrar el formulario al hacer clic en el botón
  formularioContainer.style.display = 'block';
  formularioContainer.classList.add('animate__animated', 'animate__slideInLeft');
}
// Ocultar el formulario al hacer clic en el botón Enviar
enviarBtn.addEventListener('click', function (e) {
  ocultarFormulario(e);
  enviarPregunta();
});


function enviarPregunta() {
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
  xhr.onload = function () {
    if (xhr.status === 200) {
      // La solicitud se ha completado correctamente
      const question = JSON.parse(xhr.responseText);
      const idGuesseds = question.idPersons;
      let tiempoEspera = 5000;
      let contadorCartasGiradas = 0;
      showMensajeInfoPregunta(question.question,question.answer);
      if(idGuesseds.length == 0){
        setTimeout(function () {
          cambiarTablero();
          showMensajeInfoRival();
          enviarPreguntaEnemigo();
        }, 5500);
      }
      for (const idGuess of idGuesseds) {
        setTimeout(function () {
          girarCarta(`carta-${idGuess}`);
          contadorCartasGiradas++;
          if (contadorCartasGiradas === idGuesseds.length) {
            if(comprobarVictoria(1)){
              mostrarPantallaInicio(1);
            }else{
              setTimeout(function () {
                cambiarTablero();
                showMensajeInfoRival();
                enviarPreguntaEnemigo();
              }, 1500);
            }
          }
        }, tiempoEspera);
        tiempoEspera += 500; // incrementar el tiempo de espera en 500ms por cada iteración
      }
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
}

function enviarPreguntaEnemigo() {
  // Crear una nueva solicitud AJAX
  const xhr = new XMLHttpRequest();

  // Configurar la solicitud AJAX
  xhr.open("POST", "/guessEnemy");
  xhr.setRequestHeader("Content-Type", "application/json");

  // Manejar la respuesta de la solicitud AJAX
  xhr.onload = function () {
    if (xhr.status === 200) {
      // La solicitud se ha completado correctamente
      const question = JSON.parse(xhr.responseText);
      const idGuesseds = question.idPersons;
      let tiempoEspera = 8000;
      let contadorCartasGiradas = 0;
      setTimeout(function () {
      showMensajeInfoPregunta(question.question,question.answer);
      },3000);
      for (const idGuess of idGuesseds) {
        setTimeout(function () {
          girarCarta(`carta-${idGuess}`);
          contadorCartasGiradas++;
          if (contadorCartasGiradas === idGuesseds.length) {
            if(comprobarVictoria(2)){
              mostrarPantallaInicio(2);
            }
            else {
              setTimeout(function () {
                cambiarTablero();
                showMensajeInfo();
              }, 1500);
              setTimeout(() => {
                // Ocultar el mensaje
                showDesplegablePreguntas();
              }, 4000);
            }
          }
        }, tiempoEspera);
        tiempoEspera += 500; // incrementar el tiempo de espera en 500ms por cada iteración
      }
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
}

function ocultarFormulario(event) {
  event.preventDefault();
  formularioContainer.classList.remove('animate__slideInLeft');
  formularioContainer.classList.add('animate__slideOutLeft');
  formularioContainer.addEventListener('animationend', function () {
    if (formularioContainer.classList.contains('animate__slideOutLeft')) {
      formularioContainer.classList.remove('animate__slideOutLeft');
      formularioContainer.style.display = 'none';
    }
  });
}
document.getElementById('miFormulario').addEventListener('submit', function (event) {
  animacionFormulario(event);
});

function animacionFormulario(event){
  event.preventDefault(); // Previene la acción por defecto del evento submit

  var container = document.getElementById('formulario-container');
  container.classList.add('animate__animated');
  container.classList.add('animate__slideOut');

  container.addEventListener('animationend', function () {
    container.style.display = 'none';
  });
}
// Obtener el formulario y el botón de enviar
const form = document.getElementById("miFormulario");
const submitButton = form.querySelector("button[type=submit]");

document.addEventListener("DOMContentLoaded", function () {
  cargaFormulario();
});

function cargaFormulario(){
  var opciones1 = document.getElementById("opciones1");
  var opciones2 = document.getElementById("opciones2");
  var opciones3 = document.getElementById("opciones3");
  opciones1.addEventListener("change", function () {
    var seleccion = opciones1.value;
    const opcionSombrero = opciones3.querySelector("option[value='1']");
    opcionSombrero.removeAttribute("hidden");
    for (var i = 0; i < opciones3.options.length; i++) {
      if (opciones3.options[i].value == seleccion) {
        opciones3.options[i].style.display = "none";
        if (opciones3.selectedIndex == i) {
          if (i == 0) {
            opciones3.selectedIndex = 1;
          }
          else {
            opciones3.selectedIndex = 0;
          }

        }
      } else {
        opciones3.options[i].style.display = "block";
      }
    }
  });

  opciones2.addEventListener("change", function () {
    if (opciones2.value == "n") {
      opciones3.style.display = "none";
      opciones3.selectedIndex = 0;
    } else {
      opciones3.style.display = "block";
      for (var i = 0; i < opciones3.options.length; i++) {
        var seleccion = opciones1.value;
        if (opciones3.options[i].value != seleccion) {
          opciones3.options[i].style.display = "block";
        } else {
          if (i == 0) {
            opciones3.selectedIndex = 1;
          }
        }

      }
    }
  });
}
/*Boton de confirmar seleccion secreta*/

function mostrarBotonSeleccion() {
  const boton = document.querySelector('.confirmar-seleccion');
  setTimeout(() => boton.classList.add('mostrarBoton'), 100);
}

function ocultarBotonSeleccion() {
  const boton = document.querySelector('.confirmar-seleccion');

  setTimeout(() => boton.classList.add('ocultarBotonClick'), 100);
  girarCartaSecreta('carta-secreta');
  enviarSeleccion();


  var cartas = document.querySelectorAll('.carta');
  cartas.forEach(function (carta) {
    carta.classList.remove('elegida');
    carta.style.zIndex = 1;
  });
}

/* Logica de la seleccion de carta secreta */

function cartaElegida(id) {
  clearInterval(intervalo);
  var cartaId = id.getAttribute('id');
  var cartaElegida = document.getElementById(cartaId);
  var cartas = document.querySelectorAll('.carta');
  cartas.forEach(function (carta) {
    carta.classList.remove('animar');
    carta.classList.remove('elegida');
    carta.style.zIndex = 1;
  });
  cartaElegida.classList.toggle('elegida');
  cartaElegida.style.zIndex = 10;



  var src = cartaElegida.querySelector(".carta-frontal img").getAttribute("src");
  var cartaSecreta = document.getElementById("carta-secreta");
  cartaSecreta.querySelector(".carta-trasera img").setAttribute("src", src);
}

//Enviar seleccion personaje
function enviarSeleccion() {
  var cartasFrontales = document.querySelectorAll('.carta-frontal');
  var cartaFrontal = document.querySelector('#carta-secreta .carta-trasera img');
  var srcCartaFrontal = cartaFrontal.getAttribute('src');

  var xhr = new XMLHttpRequest();
  xhr.open("POST", "/selectCharacter?srcCartaFrontal=" + srcCartaFrontal, true);
  xhr.send();
  showMensajeInfo();

  setTimeout(() => {
    // Ocultar el mensaje
    showDesplegablePreguntas();
  }, 2500);

  cartasFrontales.forEach(function(cartaFrontal) {
    cartaFrontal.removeAttribute('onclick');
  });
}

//Controlar los mensajes de informacion/turno

function showMensajeInfo() {
  const titulo = document.querySelector('#turn-modal h2');
  titulo.textContent = '¡Es tu turno!';

  const parrafo = document.querySelector('#turn-modal p');
  parrafo.innerHTML = 'Elige una pregunta para adivinar el personaje rival';

  var modal = document.getElementById("turn-modal");
  modal.style.display = "block";

  const mensajeInfo = document.querySelector('.mensajeInfo-content');
  mensajeInfo.style.backgroundColor = 'rgb(87, 180, 25, 0.5)';
  mensajeInfo.style.color = '#03ad1a';
  mensajeInfo.style.boxShadow = '0 0 20px #03ad1a';

  setTimeout(() => {
    // Ocultar el mensaje
    closeMensajeInfo();
  }, 2000);
}

function showMensajeInfoElegir() {
  const titulo = document.querySelector('#turn-modal h2');
  titulo.textContent = '¡Empieza la partida!';

  const parrafo = document.querySelector('#turn-modal p');
  parrafo.innerHTML = 'Elige a tu personaje secreto';

  var modal = document.getElementById("turn-modal");
  modal.style.display = "block";

  const mensajeInfo = document.querySelector('.mensajeInfo-content');
  mensajeInfo.style.backgroundColor = 'rgb(87, 180, 25, 0.5)';
  mensajeInfo.style.color = '#03ad1a';
  mensajeInfo.style.boxShadow = '0 0 20px #03ad1a';

  setTimeout(() => {
    // Ocultar el mensaje
    closeMensajeInfo();
  }, 2000);
}

function showMensajeInfoRival() {
  const titulo = document.querySelector('#turn-modal h2');
  titulo.textContent = '¡Es turno del rival!';

  const parrafo = document.querySelector('#turn-modal p');
  parrafo.innerHTML = 'Espera a que elija su pregunta';

  var modal = document.getElementById("turn-modal");
  modal.style.display = "block";

  const mensajeInfo = document.querySelector('.mensajeInfo-content');
  mensajeInfo.style.backgroundColor = 'rgb(182, 28, 28, 0.5)';
  mensajeInfo.style.color = '#ad0303';
  mensajeInfo.style.boxShadow = '0 0 20px #ad0303';

  setTimeout(() => {
    // Ocultar el mensaje
    closeMensajeInfo();
  }, 2000);
}
function showMensajeInfoPregunta(pregunta,respuesta) {
  const titulo = document.querySelector('#turn-modal h2');
  titulo.textContent = '';

  const parrafo = document.querySelector('#turn-modal p');
  parrafo.innerHTML = '&nbsp;';

  var modal = document.getElementById("turn-modal");
  modal.style.display = "block";

  const mensajeInfo = document.querySelector('.mensajeInfo-content');
  mensajeInfo.style.backgroundColor = 'rgb(189, 183, 107, 0.5)';
  mensajeInfo.style.color = '#f7fc00';
  mensajeInfo.style.boxShadow = '0 0 20px #f7fc00';

  let i = 0;
  let t = 2000/pregunta.length
  const intervalo = setInterval(() => {
    if (i < pregunta.length) {
      titulo.textContent += pregunta.substring(i, i+1);
      i++;
    } else {
      clearInterval(intervalo);
      let j = 0;
      const intervaloRespuesta = setInterval(() => {
        if (j < respuesta.length) {
          parrafo.innerHTML += respuesta.substring(j, j+1);
          j++;
        } else {
          clearInterval(intervaloRespuesta);
          setTimeout(() => {
            closeMensajeInfo();
          }, 100);
        }
      }, 500);
    }
  }, t);
}

function closeMensajeInfo() {
  var mensajeInfo = document.getElementById("turn-modal");
  const mensajeInfoContent = document.querySelector(".mensajeInfo-content");
  mensajeInfoContent.classList.add("hide");
  setTimeout(() => {
    mensajeInfo.style.display = "none";
    mensajeInfoContent.classList.remove("hide");
  }, 500);
}
