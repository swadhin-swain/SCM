console.log("script loaded");

let currentTheme = getTheme();
console.log(currentTheme);

// initial --->
changeTheme();

// TODO:
function changeTheme() {
    console.log(currentTheme);
    // set to web page
    document.querySelector('html').classList.add(currentTheme);

    // set the listener to change theme button
    const themeChangeButton = document.querySelector('#theme_change_button');

    // change the text
    themeChangeButton.querySelector("span").textContent = 
        currentTheme == "light" ? "Dark" : "Light";

    themeChangeButton.addEventListener("click", (event) => {
        const oldTheme = currentTheme;
        console.log("theme change button clicked");

        // change the theme
    if(currentTheme == "dark") {
        currentTheme="light";
    } else {
        currentTheme = "dark";
    }

    // local storage mein update karenge
    setTheme(currentTheme);

    // remove the current theme 
    document.querySelector("html").classList.remove(oldTheme);

    // set the current theme
    document.querySelector("html").classList.add(currentTheme);

    themeChangeButton.querySelector("span").textContent = 
    currentTheme == "light" ? "Dark" : "Light";

    });
}


// set theme to local storage
function setTheme(theme) {
    localStorage.setItem("theme",theme)
}

// get theme from local storage
function getTheme() {
    let theme = localStorage.getItem("theme");

    return theme ? theme : "light" ;
}

