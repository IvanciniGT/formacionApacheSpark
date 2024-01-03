
def saluda(nombre):             # Defino la función saluda
    print("Hola "+nombre)

texto = "Iván"                  # Defino la variable texto y haciendo que apunte a un string

saluda(texto)                   # Llamo a la función saluda pasándole la variable texto

# Esto es programación funcional vvvvvv
# En serio??? Para que quiero crear una variable que apunte a una función... so ya tengo acceso a la función directamente
mi_funcion = saluda             # Hago que la variable mi_funcion apunte a la función saluda
mi_funcion(texto)               # Llamo a la función saluda a través de la variable mi_funcion

def generar_saludo(nombre):
    return "Hola " + nombre

def imprimir_saludo(funcion_generadora_de_saludos, nombre):
    print(funcion_generadora_de_saludos(nombre))

imprimir_saludo(generar_saludo, texto)  # La función imprimir_saludo recibe como parámetro la función generar_saludo
print(generar_saludo(texto))            # La función print, recibe como parámetro el resultado de la función generar_saludo

def generar_saludo_informal(nombre):
    return "Ey! " + nombre

def generar_saludo_formal(nombre):
    return "Buenos días " + nombre

imprimir_saludo(generar_saludo, texto)  # La función imprimir_saludo recibe como parámetro la función generar_saludo
imprimir_saludo(generar_saludo_informal, texto)  # La función imprimir_saludo recibe como parámetro la función generar_saludo
imprimir_saludo(generar_saludo_formal, texto)  # La función imprimir_saludo recibe como parámetro la función generar_saludo

# En ocasiones quiero crear funciones(*) que precisan de un dato por ejemplo, 
# que se calcula a través de una función, que en momento de escribir la primera (*) desconozco