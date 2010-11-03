/* 
 * File:   ch_idsia_tools_amico_JavaPy.cc
 * Author: nikolay
 *
 * Created on October 24, 2010, 3:49 PM
 */

#include <Python.h>
#include <jni.h>
#include "ch_idsia_tools_amico_AmiCoJavaPy.h"
#include "arrayutils.h"
#include <iostream>

static std::string AMICO_WARNING = "[AmiCo Warning] : ";
static std::string AMICO_ERROR = "[AmiCo Error] : ";
static std::string AMICO_INFO = "[AmiCo Info] : ";
static std::string AMICO_EXCEPTION = "[AmiCo Exception] : ";

static int ERROR_PYTHON_IS_NOT_INITIALIZED = -1;
static int SUCCESS = 0;

PyObject* mainModule;

const char* agentName;

JNIEXPORT jint JNICALL
Java_ch_idsia_tools_amico_AmiCoJavaPy_initModule(JNIEnv* env,
                                            jobject obj,
                                            jstring moduleNameJ)
{
    std::cout << AMICO_INFO << "Initializing python environment" << std::endl;
    Py_Initialize();
    if (Py_IsInitialized())
        std::cout << AMICO_INFO << "Python environment initialized successfuly" << std::endl;
    else
    {
        std::cerr << AMICO_EXCEPTION << "Python environment initialization failed!" << std::endl;
        return ERROR_PYTHON_IS_NOT_INITIALIZED;
        //throw (AMICO_EXCEPTION + "Python environment initialization failed!");
    }

    const char* moduleName = (env)->GetStringUTFChars(moduleNameJ, NULL);
    std::cerr << moduleName << std::endl;
    PyObject* pyMod = PyString_FromString(moduleName);
//    mainModule = PyImport_Import(pyMod);
    mainModule = PyImport_ImportModule("__main__");
    std::cerr << "something done" << std::endl;
    PyObject* pp = PyImport_Import(pyMod);
    Py_DECREF(pyMod);
//    PyRun_SimpleString("import sys");
//    PyRun_SimpleString("import ctypes");
    if (pp != 0)
        std::cout << AMICO_INFO << "Main module has been loaded successfuly" << std::endl;
    else
    {
//        Py_Finalize();
        std::cout << AMICO_ERROR << "Main module had not been loaded successfuly. Details:" << std::endl;
        PyErr_Print();
        return ERROR_PYTHON_IS_NOT_INITIALIZED;
    }
    return SUCCESS;
}

JNIEXPORT void JNICALL
Java_ch_idsia_tools_amico_AmiCoJavaPy_integrateObservation(JNIEnv* env,
                                                      jobject obj,
                                                      jintArray squashedObservation,
                                                      jintArray squashedEnemies,
                                                      jfloatArray marioPos,
                                                      jfloatArray enemiesPos,
                                                      jintArray marioState)
{
    PyObject* sqObs = convertJavaArrayToPythonArray<jintArray, jint>(env, squashedObservation, 'I');
    PyObject* sqEn = convertJavaArrayToPythonArray<jintArray, jint>(env, squashedEnemies, 'I');
    PyObject* mPos = convertJavaArrayToPythonArray<jfloatArray, jfloat>(env, marioPos, 'F');
    PyObject* enPos = convertJavaArrayToPythonArray<jfloatArray, jfloat>(env, enemiesPos, 'F');
    PyObject* mState = convertJavaArrayToPythonArray<jintArray, jint>(env, marioState, 'I');
    PyObject* res = PyObject_CallMethod(mainModule, "integrateObservation", "((items)(items)(items)(items)(items))", sqObs, sqEn, mPos, enPos, mState);
}

JNIEXPORT jstring JNICALL
Java_ch_idsia_tools_amico_AmiCoJavaPy_getName(JNIEnv* env, jobject obj)
{
    PyObject* res = PyObject_CallMethod(mainModule, "getName", "()");
    const char* str = PyString_AsString(res);
    return env->NewStringUTF(str);
}

JNIEXPORT jintArray JNICALL
Java_ch_idsia_tools_amico_AmiCoJavaPy_getAction(JNIEnv* env, jobject obj)
{
    PyObject* res = PyObject_CallMethod(mainModule, "getAction", "()");

    unsigned size = (unsigned)PyTuple_Size(res);
    int* ar = new int[size];
    for (int i = 0; i < size; i++)
    {
        ar[i] = PyInt_AsLong(PyTuple_GetItem(res, i));
    }
    
    jintArray array = convertPythonArrayToJavaArray<int, jintArray, jint>(env, ar, 'I', (unsigned)PyTuple_Size(res));
    return array;
}

JNIEXPORT void JNICALL
Java_ch_idsia_tools_amico_AmiCoJavaPy_giveIntermediateReward(JNIEnv* env, jobject obj, jfloat intermediateReward)
{
    PyObject_CallMethod(mainModule, "giveIntermediateReward", "(d)", (double) intermediateReward);
}

JNIEXPORT void JNICALL
Java_ch_idsia_tools_amico_AmiCoJavaPy_reset(JNIEnv* env, jobject obj)
{
    PyObject_CallMethod(mainModule, "reset", "()");
}