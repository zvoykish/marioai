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
static int ERROR_CLASS_NOT_FOUND = -2;
static int SUCCESS = 0;

PyObject* mainModule;
PyObject* agentClass;
PyObject* classInstance;

PyObject* getActionMethod;
PyObject* getNameMethod;
PyObject* giveIntermediateRewardMethod;
PyObject* integrateObservationMethod;
PyObject* resetMethod;

const char* agentName;
const char* defaultAgentName = "AmiCo Agent";

JNIEXPORT jint JNICALL
Java_ch_idsia_tools_amico_AmiCoJavaPy_initModule(JNIEnv* env,
                                            jobject obj,
                                            jstring moduleNameJ,
                                            jstring classNameJ)
{
    std::cout << AMICO_INFO << "Initializing python environment" << std::endl;
    Py_Initialize();
    if (Py_IsInitialized())
        std::cout << AMICO_INFO << "Python environment initialized successfully" << std::endl;
    else
    {
        std::cerr << AMICO_EXCEPTION << "Python environment initialization failed!" << std::endl;
        return ERROR_PYTHON_IS_NOT_INITIALIZED;
        //throw (AMICO_EXCEPTION + "Python environment initialization failed!");
    }

    const char* moduleName = (env)->GetStringUTFChars(moduleNameJ, NULL);
    const char* className = (env)->GetStringUTFChars(classNameJ, NULL);
    mainModule = PyImport_ImportModule(moduleName);

    if (moduleName != NULL)
        std::cout << AMICO_INFO << "Main module has been loaded successfully" << std::endl;
    else
    {
        std::cerr << AMICO_ERROR << "Main module had not been loaded successfuly. Details:" << std::endl;
        PyErr_Print();
        return ERROR_PYTHON_IS_NOT_INITIALIZED;
    }

    std::cerr << moduleName << std::endl;
    std::cerr << className << std::endl;

    agentClass = PyObject_GetAttrString(mainModule, className);
//    Py_DECREF(mainModule);

    if (agentClass != NULL)
        std::cout << AMICO_INFO << "Class found successfully" << std::endl;
    else
    {
        std::cerr << AMICO_ERROR << "Class not found" << std::endl;
        PyErr_Print();
        return ERROR_CLASS_NOT_FOUND;
    }

    PyObject* args = Py_BuildValue("()");
    classInstance = PyEval_CallObject(agentClass, args);
    if (classInstance != NULL)
        std::cout << AMICO_INFO << "Class instance created successfully" << std::endl;
    else
    {
        std::cerr << AMICO_ERROR << "Class instance creation failed" << std::endl;
        PyErr_Print();
        return ERROR_CLASS_NOT_FOUND;
    }
    Py_DECREF(args);

    getNameMethod = PyObject_GetAttrString(classInstance, "getName");
    resetMethod = PyObject_GetAttrString(classInstance, "reset");
    giveIntermediateRewardMethod = PyObject_GetAttrString(classInstance, "giveIntermediateReward");
    integrateObservationMethod = PyObject_GetAttrString(classInstance, "integrateObservation");
    getActionMethod = PyObject_GetAttrString(classInstance, "getAction");

    if (getNameMethod == NULL)
        std::cerr << "getNameMethod" << std::endl;

    if (resetMethod == NULL)
        std::cerr << "resetMethod" << std::endl;

    if (giveIntermediateRewardMethod == NULL)
        std::cerr << "getIntermediate" << std::endl;

    if (integrateObservationMethod == NULL)
        std::cerr << "integrateObservation" << std::endl;

    if (getActionMethod == NULL)
        std::cerr << "getAction" << std::endl;


    Py_DECREF(agentClass);

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

        PyErr_Print();
    PyObject* args = Py_BuildValue("((items)(items)(items)(items)(items))", sqObs, sqEn, mPos, enPos, mState);
        PyErr_Print();
    PyObject* res = PyEval_CallObject(integrateObservationMethod, args);
        PyErr_Print();
    Py_DECREF(args);
        PyErr_Print();
//    PyObject* res = PyObject_CallMethod(mainModule, "integrateObservation", "((items)(items)(items)(items)(items))", sqObs, sqEn, mPos, enPos, mState);
}

JNIEXPORT jstring JNICALL
Java_ch_idsia_tools_amico_AmiCoJavaPy_getName(JNIEnv* env, jobject obj)
{
    PyObject* args = Py_BuildValue("()");
    PyObject* res = PyEval_CallObject(getNameMethod, args);

    Py_DECREF(args);
    if (res == NULL)
    {
        std::cerr << AMICO_ERROR << "Method getName has returd nothing. Using default agent name: AmiCo Agent" << std::endl;
        return env->NewStringUTF(defaultAgentName);
    }
    if (!PyString_Check(res))
    {
        std::cerr << AMICO_ERROR << "Object return by getName method is not a string. Using default agent name: AmiCo Agent" << std::endl;
        return env->NewStringUTF(defaultAgentName);
    }

    char* str = PyString_AsString(res);
    return env->NewStringUTF(str);
}

JNIEXPORT jintArray JNICALL
Java_ch_idsia_tools_amico_AmiCoJavaPy_getAction(JNIEnv* env, jobject obj)
{
    PyObject* args = Py_BuildValue("()");
    std::cerr << "here" << std::endl; std::cerr.flush();
    PyObject* res = PyEval_CallObject(getActionMethod, args);
    std::cerr << "here" << std::endl; std::cerr.flush();
    Py_DECREF(args);

    std::cerr << "here" << std::endl; std::cerr.flush();
//    if (!PyTuple_Check(res))
//    {
//        std::cerr << "here" << std::endl; std::cerr.flush();
//        std::cerr << AMICO_ERROR << "Object return by getAction method is not a tuple" << std::endl; std::cerr.flush();
//        return NULL;
//    }

    std::cerr << "here" << std::endl; std::cerr.flush();
    int size = (int)PyTuple_Size(res);
    std::cerr << "here" << std::endl; std::cerr.flush();
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
    //PyObject_CallMethod(mainModule, "giveIntermediateReward", "(d)", (double) intermediateReward);
}

JNIEXPORT void JNICALL
Java_ch_idsia_tools_amico_AmiCoJavaPy_reset(JNIEnv* env, jobject obj)
{
    PyObject* args = Py_BuildValue("()");
    PyEval_CallObject(resetMethod, args);
    Py_DECREF(args);
}