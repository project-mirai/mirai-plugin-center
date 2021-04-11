/**
 * 生成一个虚假的Plugin信息
 * @returns {{owner: {nick: string, email: string}, name: string, id: string, info: string}}
 */
export function generateFakePluginInfo() {
  return {
    id: "string",
    info: "string",
    name: "A Simple Test Plugin",
    owner: {
      email: "foo@example.com",
      nick: "Tester"
    }
  }
}

/**
 * 生成一个虚假的Plugin信息列表
 * @returns {{owner: {nick: string, email: string}, name: string, id: string, info: string}[]}
 */
export function generateFakePluginList() {
  return [generateFakePluginInfo(), generateFakePluginInfo()]
}
